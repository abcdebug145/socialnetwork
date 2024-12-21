package com.project.socialnetwork.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.project.socialnetwork.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Comment;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostLiked;
import com.project.socialnetwork.repository.CommentRepository;
import com.project.socialnetwork.repository.PostLikedRepository;
import com.project.socialnetwork.repository.PostRepository;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikedRepository postLikedRepository;
    private final CommentRepository commentRepository;
    private final ContentDetectionService contentDetectionService;
    private final ImageService imageService;
    private final NotificationRepository notificationRepository;

    public List<Post> getShuffledList() {
        return postRepository.getRandomPosts();
    }

    public List<Post> getAllPosts(Account currAccount, String keyword) {
        List<Post> posts = new ArrayList<>();
        if (!keyword.equals(""))
            posts = postRepository.search(keyword);
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
                .filter(p -> postContent.stream()
                        .anyMatch(content -> p.getContent() != null && p.getContent().contains(content)))
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
//            String content = contentDetectionService
//                    .getContent(contentDetectionService.getJsonResponse(post.getImage()));
            String response = contentDetectionService.getContent(post.getImage());
            String title = response.split("\\|")[0];
            String content = response.split("\\|")[1];
            if (post.getTitle() == null)
                post.setTitle(title);
            post.setContent(content);
        } catch (Exception e) {
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

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        try {
            if (post != null) {
                postLikedRepository.deleteByPost(post);
                commentRepository.deleteByPost(post);
                notificationRepository.deleteByPost(post);
                imageService.removeImage(getPostById(id).getImage(), "post");
                postRepository.delete(post);
            }
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }

    public List<Post> getPosts(int page, int size) {
        List<Post> allPosts = getShuffledList();
        return allPosts.subList((page - 1) * size, page * size);
    }

    public long getMaxPage() {
        return postRepository.count();
    }

    public List<Post> getPostsByAccount(Account account) {
        return postRepository.findByAccountId(account.getId());
    }
}
