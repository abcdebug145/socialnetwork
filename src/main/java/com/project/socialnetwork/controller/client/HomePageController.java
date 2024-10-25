package com.project.socialnetwork.controller.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.project.socialnetwork.service.PostService;
import com.project.socialnetwork.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {

    private final PostService postService;
    private final UploadService uploadService;

    public HomePageController(PostService postService, UploadService uploadService) {
        this.postService = postService;
        this.uploadService = uploadService;
    }

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request) {
        List<Post> posts = postService.getAllPosts();
        HttpSession session = request.getSession();
        System.out.println("USERNAME: " + session.getAttribute("username"));
        System.out.println("ID: " + session.getAttribute("id"));
        model.addAttribute("listPost", posts);
        return "client/page/homepage/index";
    }

    @GetMapping("/create-post")
    public String getCreatePostPage(Model model) {
        model.addAttribute("newPost", new Post());
        return "client/page/homepage/create-post";
    }

    @PostMapping("/create-post")
    public String createPost(@ModelAttribute("newPost") Post post, @RequestParam("postFile") MultipartFile file) {
        String imgPath = uploadService.saveUploadFile(file, "post");
        post.setImage(imgPath);
        postService.savePost(post);
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

// @Controller
// class CustomErrorController implements ErrorController {

// @RequestMapping("/error")
// public String handleError() {
// // Chuyển hướng đến trang 404
// return "redirect:/page-not-found";
// }

// }

@RestController
class APIController {
    private final PostService postService;
    private final UploadService uploadService;

    public APIController(PostService postService, UploadService uploadService) {
        this.postService = postService;
        this.uploadService = uploadService;
    }

    @PostMapping("/likePost")
    public ResponseEntity<Map<String, Object>> likePost(@RequestParam("id") Long postId,
            @RequestParam("like") boolean like) {
        System.out.println("like: " + like);
        Post post = postService.getPostById(postId);
        if (like) {
            post.setLiked(post.getLiked() + 1);
        } else {
            post.setLiked(post.getLiked() - 1);
        }
        postService.savePost(post);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", post.getLiked());
        return ResponseEntity.ok(response);
    }
}
