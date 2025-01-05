package com.project.socialnetwork.controller.client;

import java.util.*;

import com.project.socialnetwork.entity.*;
import com.project.socialnetwork.service.*;
import com.project.socialnetwork.utils.BanRequestService;
import com.project.socialnetwork.utils.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class HomePageController {

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
        if (session.getAttribute("username") != null) {
            Account currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            List<Notification> unreadNotifications = notificationService.getUnreadNotifications(currAccount);
            currAccount.setUnreadNoti(unreadNotifications.size());
            System.out.println("Unread notifications: " + unreadNotifications.size());
            return currAccount;
        }
        return new Account();
    }

    @ModelAttribute("postLiked")
    public List<PostLiked> postLiked(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            Account currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            return accountService.getPostsLiked(currAccount.getId());
        }
        return new ArrayList<>();
    }

    @ModelAttribute("listPost")
    public List<Post> listPost(HttpServletRequest request, @RequestParam("keyword") Optional<String> keyword) {
        HttpSession session = request.getSession();
        Account currAccount = null;
        if (session.getAttribute("username") != null) {
            currAccount = accountService.findByEmail(session.getAttribute("username").toString());
        }
        if (keyword.isPresent()) {
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
//        List<Post> posts = postService.getAllPosts(null, "");
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
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        List<PostLiked> postLikeds = accountService.getPostsLiked(currAccount.getId());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(postLikeds);
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("newPost") Post post, @RequestParam("postFile") MultipartFile file,
            HttpServletRequest request) {
        String imgPath = imageService.saveUploadFile(file, "post");
        post.setImage(imgPath);
        post.setLikeCount(0);
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        postService.createPost(currAccount, post);
        return "redirect:/";
    }

    @GetMapping("/profile/{username}")
    public String getProfilePage(Model model, @PathVariable("username") String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account currAccount = null;
        if (session.getAttribute("username") != null) {
            currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            model.addAttribute("postLiked", accountService.getPostsLiked(currAccount.getId()));
        }
        Account account = accountService.findByUsername(username);
        List<Post> posts = postService.getAllPostsByAccount(account);
        model.addAttribute("account", account);
        model.addAttribute("listPost", posts);
        return "client/page/user/user-profile";
    }

    @GetMapping("/profile/edit-profile")
    public String getEditProfilePage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        model.addAttribute("account", currAccount);
        return "client/page/user/edit-profile";
    }

    @PostMapping("/profile/edit-profile")
    public String saveProfile(@ModelAttribute("account") Account account, HttpServletRequest request,
                              @RequestParam("avatarFile") MultipartFile avatar) {
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        currAccount.setAddress(account.getAddress());
        session.setAttribute("username", currAccount.getEmail());
        currAccount.setUsername(account.getUsername());
        currAccount.setFullName(account.getFullName());
        currAccount.setAbout(account.getAbout());
        if (!avatar.getOriginalFilename().equals(""))
            currAccount.setAvatar(accountService.newAvatar(currAccount.getAvatar(), avatar));
        accountService.saveAccount(currAccount);
        return "redirect:/profile/edit-profile";
    }

    @PostMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Account account = request.getSession().getAttribute("username") != null
                ? accountService.findByEmail(request.getSession().getAttribute("username").toString())
                : null;
        postService.deletePost(postId);
        return "redirect:/profile/" + account.getUsername();
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
        Account currAccount = accountService.findByEmail(session.getAttribute("username").toString());
        Post post = postService.getPostById(postId);
        postService.likePost(post, currAccount);
        post.setLikeCount(post.getPostLikeds().size());
        postService.savePost(post);
        if (like)
            notificationService.createNotification(currAccount, post, "like");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", post.getLikeCount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createComment")
    public ResponseEntity<Map<String, Object>> createComment(@RequestParam("postId") Long postId,
                                                             @RequestParam("comment") String comment,
                                                             HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account currAccount = accountService.findByEmail(session.getAttribute("username").toString());
        Post post = postService.getPostById(postId);
        postService.createComment(comment, currAccount, post);
        notificationService.createNotification(currAccount, post, "comment");
        Map<String, Object> response = new HashMap<>();
        response.put("username", currAccount.getUsername());
        response.put("content", comment);
        response.put("time", new Date());
        return ResponseEntity.ok(response);
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
