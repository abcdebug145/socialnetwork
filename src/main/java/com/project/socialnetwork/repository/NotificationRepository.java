package com.project.socialnetwork.repository;

import java.util.List;

import com.project.socialnetwork.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.account.id != :accountId")
    List<Notification> findNotificationsByAccount(Long accountId);

    void deleteByPost(Post post);
}
