package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Notification;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications(Long accountId) {
        return notificationRepository.findByAccount_Id(accountId);
    }

    public List<Notification> getAllUnreadNotifications(Long accountId) {
        return notificationRepository.findUnreadNotiByAccount_Id(accountId);
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
}