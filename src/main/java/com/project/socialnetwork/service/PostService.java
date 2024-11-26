package com.project.socialnetwork.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Comment;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.repository.CommentRepository;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;
    private final CommentRepository commentRepository;
    private final ContentDetectionService contentDetectionService;
    private final ImageService imageService;

    public PostService(PostRepository postRepository, PostLikedRepository postLikedRepository,
                       CommentRepository commentRepository, ContentDetectionService contentDetectionService, ImageService imageService) {
        this.postRepository = postRepository;
        this.postLikedRepository = postLikedRepository;
        this.commentRepository = commentRepository;
        this.contentDetectionService = contentDetectionService;
        this.imageService = imageService;
    }

    public List<Post> getAllPosts(Account currAccount, String keyword) {
        List<Post> posts = new ArrayList<>();
        if (!keyword.equals(""))
            posts = postRepository.search(keyword);
        else
            posts = postRepository.findAll();
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

    public List<Post> getAllSimilarPosts(Post post) {
        Set<String> postContent;
        if (post.getContent() != null) {
            postContent = new HashSet<>(Arrays.asList(post.getContent()
                    .replaceAll("[\\[\\]]", "").split(",\\s*")));
        } else {
            postContent = new HashSet<>();
        }
        List<Post> postList = postRepository.findAll();
        List<Post> similarPosts = postList.stream()
                .filter(p -> postContent.stream().anyMatch(content -> p.getContent() != null && p.getContent().contains(content)))
                .collect(Collectors.toList());
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
            String content = contentDetectionService
                    .getContent(contentDetectionService.getJsonResponse(post.getImage()));
            post.setContent(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public void deletePost(Long id) {
        postRepository.deleteById(id);
        commentRepository.deleteCommentsByPost(getPostById(id));
        postLikedRepository.deleteByPostId(id);
        imageService.removeImage(getPostById(id).getImage(), "post");
    }

    public List<Post> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAll(pageable).getContent();
    }
}
