package com.project.socialnetwork.controller.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Comment;
import com.project.socialnetwork.domain.Notification;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.CommentService;
import com.project.socialnetwork.service.ImageService;
import com.project.socialnetwork.service.NotificationService;
import com.project.socialnetwork.service.PostService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomePageController {

    private final PostService postService;
    private final ImageService imageService;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final CommentService commentService;

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request,
            @RequestParam("keyword") Optional<String> keyword) {
        List<PostLiked> postLikeds = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();
        Account currAccount = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            notifications = notificationService.getAllNotifications(currAccount);
            currAccount.setUnreadNoti(notifications.size());
            postLikeds = accountService.getPostsLiked(currAccount.getId());
            model.addAttribute("postLiked", postLikeds);
        }
        notificationService.sendNotification("New user has logged in");
        if (keyword.isPresent()) {
            model.addAttribute("listPost", postService.getAllPosts(currAccount, keyword.get()));
        }

        model.addAttribute("newPost", new Post());
        model.addAttribute("account", currAccount);
        model.addAttribute("keyword", keyword.isPresent() ? keyword.get() : "");
        model.addAttribute("notifications", notifications);
        return "client/page/homepage/index";
    }

    @GetMapping("/post/{id}")
    public String getDetailPost(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account currAccount = null;
        if (session.getAttribute("username") != null) {
            currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            model.addAttribute("postLiked", accountService.getPostsLiked(currAccount.getId()));
        }
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getAllComment(post);
        List<Post> similarPosts = postService.getAllSimilarPosts(post);

        model.addAttribute("account", currAccount);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("listPost", similarPosts);
        return "client/page/homepage/view-post";
    }

    @GetMapping("/loadMorePosts")
    public ResponseEntity<List<Post>> loadMorePosts(@RequestParam("page") long page, @RequestParam("size") int size,
            HttpServletRequest request) {
        long maxPage = (long) Math.ceil((double) (postService.getMaxPage() / 40));
        List<Post> posts = postService.getPosts((int) (page % maxPage), size);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(posts);
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
        List<Post> posts = postService.getPostsByAccount(account);
        model.addAttribute("currAccount", currAccount);
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
        return "client/page/auth/404";
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

}
