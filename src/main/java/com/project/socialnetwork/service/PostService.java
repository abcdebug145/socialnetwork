package com.project.socialnetwork.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public PostService(PostRepository postRepository, PostLikedRepository postLikedRepository,
                       CommentRepository commentRepository, ContentDetectionService contentDetectionService) {
        this.postRepository = postRepository;
        this.postLikedRepository = postLikedRepository;
        this.commentRepository = commentRepository;
        this.contentDetectionService = contentDetectionService;
    }

    public List<Post> getAllPosts(Account currAccount, String keyword) {
        List<Post> posts = new ArrayList<>();
        if (!keyword.equals(""))
            posts = postRepository.search(keyword);
        else
            posts = postRepository.findAll();
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

    public List<Post> getAllSimilarPosts(List<Tag> tags) {
        Set<Post> posts = new HashSet<>();
        for (Tag tag : tags) {
            List<PostTag> postTags = postTagRepository.findByTag(tag);
            for (PostTag postTag : postTags) {
                posts.add(postTag.getPost());
            }
        }
        List<Post> postList = new ArrayList<>(posts);
        Collections.shuffle(postList);
        return postList;
    }
    
    // public List<Post> getAllSimilarPosts(Post post) {
    // List<Post> post
    // Collections.shuffle(postList);
    // return postList;
    // }

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
}
