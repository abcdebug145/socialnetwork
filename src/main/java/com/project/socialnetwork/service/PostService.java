package com.project.socialnetwork.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Comment;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;
import com.project.socialnetwork.domain.PostTag;
import com.project.socialnetwork.domain.Tag;
import com.project.socialnetwork.repository.CommentRepository;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.PostTagRepository;
import com.project.socialnetwork.repository.TagRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;
    private final CommentRepository commentRepository;
    private final TagDetectionService tagDetectionService;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, PostLikedRepository postLikedRepository,
                       CommentRepository commentRepository, TagDetectionService tagDetectionService,
                       PostTagRepository postTagRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postLikedRepository = postLikedRepository;
        this.commentRepository = commentRepository;
        this.tagDetectionService = tagDetectionService;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void createPost(Account account, Post post) {
        post.setAccount(account);
        postRepository.save(post);
        try {
            List<String> tags = tagDetectionService.getTags(tagDetectionService.getJsonResponse(post.getImage()));
            List<PostTag> postTags = new ArrayList<>();
            for (String item : tags) {
                PostTag postTag = new PostTag();
                postTag.setPost(post);
                Tag tag = tagRepository.findByTag(item);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTag(item);
                    tagRepository.save(tag);
                }
                postTag.setTag(tag);
                postTags.add(postTag);
            }
            postTagRepository.saveAll(postTags);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public List<Tag> getAllTagsByPost(Post post) {
        List<PostTag> postTags = postTagRepository.findAllByPost(post);
        List<Tag> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            tags.add(postTag.getTag());
        }
        return tags;
    }
}
