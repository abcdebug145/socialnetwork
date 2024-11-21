package com.project.socialnetwork.controller.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Comment;
import com.project.socialnetwork.domain.Notification;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.domain.Tag;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.CommentService;
import com.project.socialnetwork.service.NotificationService;
import com.project.socialnetwork.service.PostService;
import com.project.socialnetwork.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomePageController {

    private final PostService postService;
    private final UploadService uploadService;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final CommentService commentService;

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request,
                              @RequestParam("keyword") Optional<String> keyword, @RequestParam("postId") Optional<Long> postId) {
        Post newPost = new Post();
        List<PostLiked> postLiked = new ArrayList<PostLiked>();
        List<Notification> notifications = new ArrayList<>();
        Account currAccount = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            currAccount = accountService.findByEmail(username);
            postLiked = accountService.getPostsLiked(currAccount.getId());
            notifications = notificationService.getAllNotifications(currAccount.getId());
            currAccount.setUnreadNoti(notifications.size());
        }
        List<Post> posts = (keyword.isPresent()) ? postService.getAllPosts(currAccount, keyword.get())
                : postService
                .getAllPosts(currAccount, "");
        if (postId.isPresent()) {
            Post post = postService.getPostById(postId.get());
            List<Comment> comments = commentService.getAllComment(post);
            List<Tag> tags = postService.getAllTagsByPost(post);
            posts = postService.getAllSimilarPosts(tags);
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
        }
        model.addAttribute("listPost", posts);
        model.addAttribute("newPost", newPost);
        model.addAttribute("postLiked", postLiked);
        model.addAttribute("account", currAccount);
        model.addAttribute("keyword", keyword.isPresent() ? keyword.get() : "");
        model.addAttribute("notifications", notifications);
        return "client/page/homepage/index";
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("newPost") Post post, @RequestParam("postFile") MultipartFile file,
                             HttpServletRequest request) {
        String imgPath = uploadService.saveUploadFile(file, "post");
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

    @PostMapping("/deletePost")
    public ResponseEntity<Map<String, Object>> deletePost(@RequestParam("id") Long postId) {
        Post post = postService.getPostById(postId);
        postService.savePost(post);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
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
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
