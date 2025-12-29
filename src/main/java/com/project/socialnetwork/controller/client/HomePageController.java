package com.project.socialnetwork.controller.client;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.socialnetwork.entity.*;
import com.project.socialnetwork.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomePageController {

    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    private final PostService postService;
    private final ImageService imageService;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final CommentService commentService;
    private final BanRequestService banRequestService;

    @ModelAttribute("newPost")
    public Post newPost() {
        return new Post();
    }

    @ModelAttribute("currAccount")
    public Account account(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr != null) {
            try {
                Account currAccount = accountService.findByEmail(usernameAttr.toString());
                if (currAccount != null) {
                    List<Notification> unreadNotifications = notificationService.getUnreadNotifications(currAccount);
                    currAccount.setUnreadNoti(unreadNotifications.size());
                    logger.debug("Unread notifications for user {}: {}", currAccount.getEmail(), unreadNotifications.size());
                    return currAccount;
                }
            } catch (Exception e) {
                logger.warn("Error loading current account from session", e);
            }
        }
        return new Account();
    }

    @ModelAttribute("postLiked")
    public List<PostLiked> postLiked(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr != null) {
            try {
                Account currAccount = accountService.findByEmail(usernameAttr.toString());
                if (currAccount != null) {
                    return accountService.getPostsLiked(currAccount.getId());
                }
            } catch (Exception e) {
                logger.warn("Error loading liked posts from session", e);
            }
        }
        return new ArrayList<>();
    }

    @ModelAttribute("listPost")
    public List<Post> listPost(HttpServletRequest request, @RequestParam("keyword") Optional<String> keyword) {
        HttpSession session = request.getSession();
        Account currAccount = null;
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr != null) {
            try {
                currAccount = accountService.findByEmail(usernameAttr.toString());
            } catch (Exception e) {
                logger.warn("Error loading current account for post list", e);
            }
        }
        if (keyword.isPresent() && !keyword.get().isEmpty()) {
            return postService.getAllPosts(currAccount, keyword.get());
        }
        return new ArrayList<>();
    }

    @ModelAttribute("keyword")
    public String keyword(@RequestParam("keyword") Optional<String> keyword) {
        return keyword.isPresent() ? keyword.get() : "";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "client/page/homepage/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword) {
        return "client/page/homepage/search";
    }

    @GetMapping("/post/{id}")
    public String getDetailPost(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getAllComment(post);
        List<Post> similarPosts = postService.getAllSimilarPosts(post, "");

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("listPost", similarPosts);
        return "client/page/homepage/view-post";
    }

    @GetMapping("/loadMorePosts")
    public ResponseEntity<List<Post>> loadMorePosts(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<Post> posts = postService.getPosts(page, size);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(posts);
    }

    @GetMapping("/getSimilarPosts")
    public ResponseEntity<List<Post>> getSimilarPosts(@RequestParam("postId") Optional<Long> postId,
            @RequestParam("keyword") Optional<String> keyword, @RequestParam("page") long page,
            @RequestParam("size") int size) {
        Post post = postId.isPresent() ? postService.getPostById(postId.get()) : null;
        String kw = keyword.isPresent() ? keyword.get() : "";

        List<Post> similarPosts = postService.getAllSimilarPosts(post, kw);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(similarPosts);
    }

    @GetMapping("/getPostLikedByAccount")
    public ResponseEntity<List<PostLiked>> getPostLikedByAccount(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ArrayList<>());
        }
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount != null) {
                List<PostLiked> postLikeds = accountService.getPostsLiked(currAccount.getId());
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(postLikeds);
            }
        } catch (Exception e) {
            logger.error("Error getting liked posts for account", e);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ArrayList<>());
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("newPost") Post post, @RequestParam("postFile") MultipartFile file,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return "redirect:/login";
        }
        
        try {
            String imgPath = imageService.saveUploadFile(file, "post");
            if (imgPath == null || imgPath.isEmpty()) {
                logger.warn("Failed to upload image for post");
                return "redirect:/";
            }
            post.setImage(imgPath);
            post.setLikeCount(0);
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount != null) {
                postService.createPost(currAccount, post);
            }
        } catch (Exception e) {
            logger.error("Error creating post", e);
        }
        return "redirect:/";
    }

    @GetMapping("/profile/{username}")
    public String getProfilePage(Model model, @PathVariable("username") String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr != null) {
            try {
                Account currAccount = accountService.findByEmail(usernameAttr.toString());
                if (currAccount != null) {
                    model.addAttribute("postLiked", accountService.getPostsLiked(currAccount.getId()));
                }
            } catch (Exception e) {
                logger.warn("Error loading current account for profile page", e);
            }
        }
        
        Account account = accountService.findByUsername(username);
        if (account == null) {
            return "redirect:/";
        }
        
        List<Post> posts = postService.getAllPostsByAccount(account);
        model.addAttribute("account", account);
        model.addAttribute("listPost", posts);
        return "client/page/user/user-profile";
    }

    @GetMapping("/profile/edit-profile")
    public String getEditProfilePage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return "redirect:/login";
        }
        
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount != null) {
                model.addAttribute("account", currAccount);
                return "client/page/user/edit-profile";
            }
        } catch (Exception e) {
            logger.error("Error loading edit profile page", e);
        }
        return "redirect:/";
    }

    @PostMapping("/profile/edit-profile")
    public String saveProfile(@ModelAttribute("account") Account account, HttpServletRequest request,
                              @RequestParam("avatarFile") MultipartFile avatar) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return "redirect:/login";
        }
        
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount == null) {
                return "redirect:/login";
            }
            
            currAccount.setAddress(account.getAddress());
            session.setAttribute("username", currAccount.getEmail());
            currAccount.setUsername(account.getUsername());
            currAccount.setFullName(account.getFullName());
            currAccount.setAbout(account.getAbout());
            
            if (avatar != null && avatar.getOriginalFilename() != null && !avatar.getOriginalFilename().isEmpty()) {
                currAccount.setAvatar(accountService.newAvatar(currAccount.getAvatar(), avatar));
            }
            accountService.saveAccount(currAccount);
        } catch (Exception e) {
            logger.error("Error saving profile", e);
        }
        return "redirect:/profile/edit-profile";
    }

    @PostMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return "redirect:/login";
        }
        
        try {
            Account account = accountService.findByEmail(usernameAttr.toString());
            if (account != null) {
                postService.deletePost(postId);
                return "redirect:/profile/" + account.getUsername();
            }
        } catch (Exception e) {
            logger.error("Error deleting post {}", postId, e);
        }
        return "redirect:/";
    }

    @GetMapping("/page-not-found")
    public String getPage() {
        return "client/page/auth/403";
    }

    // --------------------- API ---------------------

    @PostMapping("/likePost")
    public ResponseEntity<Map<String, Object>> likePost(@RequestParam("id") Long postId,
                                                        @RequestParam("like") boolean like, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(errorResponse);
        }
        
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            Post post = postService.getPostById(postId);
            if (currAccount == null || post == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Account or post not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            postService.likePost(post, currAccount);
            post.setLikeCount(post.getPostLikeds().size());
            postService.savePost(post);
            if (like) {
                notificationService.createNotification(currAccount, post, "like");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("likeCount", post.getLikeCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error liking post {}", postId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Internal server error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/createComment")
    public ResponseEntity<Map<String, Object>> createComment(@RequestParam("postId") Long postId,
                                                             @RequestParam("comment") String comment,
                                                             HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(errorResponse);
        }
        
        if (comment == null || comment.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Comment cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            Post post = postService.getPostById(postId);
            if (currAccount == null || post == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Account or post not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            postService.createComment(comment.trim(), currAccount, post);
            notificationService.createNotification(currAccount, post, "comment");
            Map<String, Object> response = new HashMap<>();
            response.put("username", currAccount.getUsername());
            response.put("content", comment.trim());
            response.put("time", new Date());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating comment for post {}", postId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @PostMapping("/submitReport")
    public String submitReport(
            @RequestParam("postId") Long postId,
            @RequestParam("reason") String reason,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        // Ensure user is authenticated
        String username = (String) session.getAttribute("username");
        if (username == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to report a post.");
            return "redirect:/login";
        }

        // Fetch user and post details
        Account currAccount = accountService.findByEmail(username);
        Post post = postService.getPostById(postId);

        if (post == null) {
            redirectAttributes.addFlashAttribute("error", "Post not found.");
            return "redirect:/";
        }

        // Process the report
        banRequestService.createBanRequest(currAccount, post, reason);

        // Success message
        redirectAttributes.addFlashAttribute("success", "Your report has been submitted successfully.");
        return "redirect:/";
    }

}
