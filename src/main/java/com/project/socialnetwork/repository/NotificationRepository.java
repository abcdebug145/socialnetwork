package com.project.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccount_Id(Long account_id);

    // @Query(value = "SELECT n FROM Notification n WHERE n.account.id = ?1 AND
    // n.isRead = false")
    // List<Notification> findUnreadNotiByAccount_Id(Long accountId);

    @Query("SELECT n FROM Notification n WHERE n.account.id != :accountId AND n.post.id IN (SELECT p.id FROM Post p WHERE p.account.id = :accountId)")
    List<Notification> findNotificationsExcludingAccountAndPosts(Long accountId);
}