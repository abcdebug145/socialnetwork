package com.project.socialnetwork.controller.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.socialnetwork.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class NotificationController {
    private final AccountService accountService;
    private final NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/like")
    public void sendLikedNoti(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = sha.getUser().getName() + " liked your post";

        simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }

    @MessageMapping("/comment")
    public void sendCommentNoti(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = sha.getUser().getName() + " commented in your post";

        simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }

    @MessageMapping("/ban")
    public void sendBanNoti(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = "Your account has been banned";

        simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }

    @PostMapping("/resetUnreadNoti")
    public ResponseEntity<Map<String, Object>> resetUnreadNoti(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account account = accountService.findByEmail((String) session.getAttribute("username"));
        account.setUnreadNoti(0);
        accountService.saveAccount(account);
        notificationService.resetUnreadNoti(account);
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getNotifications")
    public ResponseEntity<List<Notification>> notifications(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account currAccount = null;
        if (session.getAttribute("username") != null) {
            currAccount = accountService.findByEmail(session.getAttribute("username").toString());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(notificationService.getAllNotifications(currAccount));
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ArrayList<>());
    }

    @GetMapping("/getUnreadNoti")
    public ResponseEntity<String> getUnreadNoti(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int num = notificationService
                .getUnreadNotifications(accountService.findByEmail((String) session.getAttribute("username"))).size();
        return ResponseEntity.ok(String.valueOf(num));
    }

}
