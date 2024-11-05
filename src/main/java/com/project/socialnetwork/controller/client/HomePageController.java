package com.project.socialnetwork.controller.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.PostService;
import com.project.socialnetwork.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {

    private final PostService postService;
    private final UploadService uploadService;
    private final AccountService accountService;

    public HomePageController(PostService postService, UploadService uploadService, AccountService accountService) {
        this.postService = postService;
        this.uploadService = uploadService;
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request,
            @RequestParam("keyword") Optional<String> keyword) {
        Post newPost = new Post();
        List<PostLiked> postLiked = new ArrayList<PostLiked>();
        Account currAccount = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            currAccount = accountService.findByEmail(username);
            postLiked = accountService.getPostsLiked(currAccount.getId());
        }
        List<Post> posts = (keyword.isPresent()) ? postService.getAllPosts(currAccount, keyword.get())
                : postService
                        .getAllPosts(currAccount, "");
        model.addAttribute("listPost", posts);
        model.addAttribute("newPost", newPost);
        model.addAttribute("postLiked", postLiked);
        model.addAttribute("keyword", keyword.isPresent() ? keyword.get() : "");
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

    @GetMapping("/profile/edit-profile")
    public String getEditProfilePage(Model model) {
        model.addAttribute("editAccount", new Account());
        return "client/page/user/edit-profile";
    }

    @GetMapping("/page-not-found")
    public String getPage() {
        return "client/page/auth/404";
    }
}

@RestController
class APIController {
    private final PostService postService;
    private final AccountService accountService;

    public APIController(PostService postService, AccountService accountService) {
        this.postService = postService;
        this.accountService = accountService;
    }

    @PostMapping("/likePost")
    public ResponseEntity<Map<String, Object>> likePost(@RequestParam("id") Long postId,
            @RequestParam("like") boolean like, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account currAccount = accountService.findByEmail(session.getAttribute("username").toString());
        Post post = postService.getPostById(postId);
        postService.likePost(post, currAccount);
        post.setLikeCount(post.getPostLikeds().size());
        postService.savePost(post);

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

}
