package com.project.socialnetwork.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Comment;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.repository.CommentRepository;
import com.project.socialnetwork.utils.TimeUtils;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComment(Post post) {
        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment comment : comments) {
            comment.setTimeAgo(TimeUtils.calculateTimeAgo(comment.getDate()));
        }
        Collections.reverse(comments);
        return comments;
    }
}
