package com.project.socialnetwork.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Notification;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.repository.NotificationRepository;
import com.project.socialnetwork.utils.TimeUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Notification> getAllNotifications(Account account) {
        List<Notification> notifications = notificationRepository.findNotificationsByAccount(account.getId());
        for (Notification noti : notifications) {
            noti.setTimeAgo(TimeUtils.calculateTimeAgo(noti.getDate()));
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
        String message = switch (type) {
            case "comment" -> " commented in your post ";
            case "like" -> " liked your post";
            default -> "";
        };
        Notification noti = new Notification();
        noti.setAccount(account);
        noti.setPost(post);
        noti.setMessage(message);
        noti.setRead(false);
        notificationRepository.save(noti);
    }
}