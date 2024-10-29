package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.repository.AccountRepository;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;
    private final AccountRepository accountRepository;

    public PostService(PostRepository postRepository, PostLikedRepository postLikedRepository,
            AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.postLikedRepository = postLikedRepository;
        this.accountRepository = accountRepository;
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

    public void createPost(Account account, Post post) {
        post.setAccount(account);
        postRepository.save(post);
    }

    public void likePost(Post post, Account account) {
        PostLiked postLiked = postLikedRepository.findByAccountAndPost(account, post);
        if (postLiked == null) {
            postLiked = new PostLiked();
            postLiked.setAccount(account);
            postLiked.setPost(post);
            postLikedRepository.save(postLiked);
        } else {
            postLikedRepository.delete(postLiked);
        }
    }
}
