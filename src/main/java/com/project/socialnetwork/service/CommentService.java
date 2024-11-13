package com.project.socialnetwork.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment comment : comments) {
            comment.setTimeAgo(calculateTimeAgo(comment.getDate()));
        }
        return comments;
    }

    private String calculateTimeAgo(Date commentDate) {
        long duration = new Date().getTime() - commentDate.getTime();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

        final int SECONDS_IN_A_MINUTE = 60;
        final int SECONDS_IN_AN_HOUR = 60 * 60;
        final int SECONDS_IN_A_DAY = 24 * 60 * 60;

        if (seconds < SECONDS_IN_A_MINUTE) {
            return "Just now";
        } else if (seconds < SECONDS_IN_AN_HOUR) {
            return seconds / SECONDS_IN_A_MINUTE + " minutes ago";
        } else if (seconds < SECONDS_IN_A_DAY) {
            return seconds / SECONDS_IN_AN_HOUR + " hours ago";
        } else {
            return seconds / SECONDS_IN_A_DAY + " days ago";
        }
    }
}
