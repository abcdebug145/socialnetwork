package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Comment;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComment(Post post) {
        return commentRepository.findByPost(post);
    }
}
