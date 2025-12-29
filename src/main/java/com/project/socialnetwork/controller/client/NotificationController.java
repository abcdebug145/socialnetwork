package com.project.socialnetwork.controller.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.project.socialnetwork.entity.Notification;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("isSuccess", false);
            errorResponse.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(errorResponse);
        }
        
        try {
            Account account = accountService.findByEmail(usernameAttr.toString());
            if (account == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("isSuccess", false);
                errorResponse.put("error", "Account not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            account.setUnreadNoti(0);
            accountService.saveAccount(account);
            notificationService.resetUnreadNoti(account);
            Map<String, Object> response = new HashMap<>();
            response.put("isSuccess", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error resetting unread notifications", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("isSuccess", false);
            errorResponse.put("error", "Internal server error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/getNotifications")
    public ResponseEntity<List<Notification>> notifications(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr != null) {
            try {
                Account currAccount = accountService.findByEmail(usernameAttr.toString());
                if (currAccount != null) {
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(notificationService.getAllNotifications(currAccount));
                }
            } catch (Exception e) {
                logger.error("Error getting notifications", e);
            }
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ArrayList<>());
    }

    @GetMapping("/getUnreadNoti")
    public ResponseEntity<String> getUnreadNoti(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return ResponseEntity.ok("0");
        }
        
        try {
            Account account = accountService.findByEmail(usernameAttr.toString());
            if (account == null) {
                return ResponseEntity.ok("0");
            }
            int num = notificationService.getUnreadNotifications(account).size();
            return ResponseEntity.ok(String.valueOf(num));
        } catch (Exception e) {
            logger.error("Error getting unread notifications count", e);
            return ResponseEntity.ok("0");
        }
    }

}
