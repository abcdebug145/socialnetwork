package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;

    public PostService(PostRepository postRepository, PostLikedRepository postLikedRepository) {
        this.postRepository = postRepository;
        this.postLikedRepository = postLikedRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public List<Account> getAllUsersLikedPost(Long postId) {
        return postLikedRepository.findAllByPosts_Id(postId);
    }
}
