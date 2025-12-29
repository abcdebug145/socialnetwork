package com.project.socialnetwork.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Comment;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostLiked;
import com.project.socialnetwork.repository.CommentRepository;
import com.project.socialnetwork.repository.NotificationRepository;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private static final String CONTENT_SEPARATOR = "\\|";
    private static final String CONTENT_PATTERN = "[\\[\\]]";
    private static final String CONTENT_SPLIT_PATTERN = ",\\s*";

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;
    private final CommentRepository commentRepository;
    private final ContentDetectionService contentDetectionService;
    private final ImageService imageService;
    private final NotificationRepository notificationRepository;

    public List<Post> getAllPosts(Account currAccount, String keyword) {
        List<Post> posts = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            posts = postRepository.search(keyword);
        }
        Collections.shuffle(posts);
        if (currAccount != null) {
            List<Post> temp = new ArrayList<>();
            Iterator<Post> iterator = posts.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                if (postLikedRepository.findByPostId(post.getId()) != null) {
                    temp.add(post);
                    iterator.remove();
                }
            }
            posts.addAll(temp);
        }
        return posts;
    }

    public List<Post> getAllPostsByAccount(Account account) {
        return postRepository.findByAccountId(account.getId());
    }

    public List<Post> getAllSimilarPosts(Post post, String keyword) {
        if (post == null) {
            return new ArrayList<>();
        }
        
        Set<String> postContent;
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            postContent = new HashSet<>(Arrays.asList(post.getContent()
                    .replaceAll(CONTENT_PATTERN, "").split(CONTENT_SPLIT_PATTERN)));
        } else {
            postContent = new HashSet<>();
        }
        if (keyword != null && !keyword.isEmpty()) {
            postContent.add(keyword);
        }

        List<Post> postList = postRepository.findAll();
        List<Post> similarPosts = postList.stream()
                .filter(p -> p.getContent() != null && postContent.stream()
                        .anyMatch(content -> p.getContent().contains(content)))
                .collect(Collectors.toList());
        similarPosts.remove(post);
        Collections.shuffle(similarPosts);
        return similarPosts;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void createPost(Account account, Post post) {
        post.setAccount(account);
        try {
            String response = contentDetectionService.getContent(post.getImage());
            if (response != null && !response.isEmpty() && response.contains(CONTENT_SEPARATOR.replace("\\", ""))) {
                String[] parts = response.split(CONTENT_SEPARATOR);
                if (parts.length >= 2) {
                    String title = parts[0];
                    String content = parts[1];
                    if (post.getTitle() == null || post.getTitle().isEmpty()) {
                        post.setTitle(title);
                    }
                    post.setContent(content);
                } else {
                    logger.warn("Content detection response format unexpected: {}", response);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing content detection for post image: {}", post.getImage(), e);
        }
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

    public void createComment(String content, Account account, Post post) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAccount(account);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            logger.warn("Attempted to delete post with id {} but post not found", id);
            return;
        }
        
        try {
            postLikedRepository.deleteByPost(post);
            commentRepository.deleteByPost(post);
            notificationRepository.deleteByPost(post);
            if (post.getImage() != null && !post.getImage().isEmpty()) {
                imageService.removeImage(post.getImage(), "post");
            }
            postRepository.delete(post);
            logger.info("Successfully deleted post with id {}", id);
        } catch (Exception e) {
            logger.error("Error deleting post with id {}", id, e);
            throw e;
        }
    }

    public List<Post> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = new ArrayList<>(postRepository.findAll(pageable).getContent());
        Collections.shuffle(posts);
        return posts;
    }

}
