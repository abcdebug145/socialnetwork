package com.project.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.entity.Notification;
import com.project.socialnetwork.entity.Post;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.post.account.id = :accountId and" +
            " n.account.id != :accountId ORDER BY n.date DESC")
    List<Notification> findNotificationsByAccount(Long accountId);

    void deleteByPost(Post post);
}
