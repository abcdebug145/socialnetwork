package com.project.socialnetwork.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

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
}
