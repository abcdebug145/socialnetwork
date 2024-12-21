package com.project.socialnetwork.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Notification;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.repository.NotificationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostService postService;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Notification> getAllNotifications(Account account) {
        List<Notification> notifications = notificationRepository.findNotificationsByAccount(account.getId());
        for (Notification noti : notifications) {
            noti.setTimeAgo(calculateTimeAgo(noti.getDate()));
        }
        return notifications;
    }

    public List<Notification> getUnreadNotifications(Account account) {
        List<Notification> notifications = getAllNotifications(account);
        List<Notification> unreadNotifications = new ArrayList<>();
        for (Notification noti : notifications) {
            if (!noti.isRead()) {
                unreadNotifications.add(noti);
            }
        }
        return unreadNotifications;
    }

    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void resetUnreadNoti(Account account) {
        List<Notification> notifications = getUnreadNotifications(account);
        for (Notification noti : notifications) {
            noti.setRead(true);
            notificationRepository.save(noti);
        }
    }

    public void createNotification(Account account, Post post, String type) {
        String message = "";
        switch (type) {
            case "comment":
                message = account.getUsername() + " commented in your post ";
                break;
            case "like":
                message = account.getUsername() + " liked your post";
                break;
        }
        Notification noti = new Notification();
        noti.setAccount(account);
        noti.setPost(post);
        noti.setMessage(message);
        noti.setRead(false);
        notificationRepository.save(noti);
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