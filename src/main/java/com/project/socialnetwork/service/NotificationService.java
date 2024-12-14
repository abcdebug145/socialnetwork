package com.project.socialnetwork.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Notification;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Notification> getAllNotifications(Account account) {
        List<Notification> temp = notificationRepository.findNotificationsByAccount(account.getId());
        List<Post> posts = postService.getAllPostsByAccount(account);
        List<Notification> notifications = new ArrayList<>();
        for (Post post : posts) {
            for (Notification noti : temp) {
                if (noti.getPost().getId() == post.getId()) {
                    notifications.add(noti);
                }
            }
        }
        return notifications;
    }

    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    // public List<Notification> getAllUnreadNotifications(Long accountId) {
    // return notificationRepository.findUnreadNotiByAccount_Id(accountId);
    // }

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
}