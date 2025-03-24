package com.helio.controller;

import com.helio.model.WebSocketMessage;
import com.helio.service.MessagingService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class WebSocketController {

        private final MessagingService messagingService;

        @MessageMapping("/send-message")
        @SendTo("/topic/ws-messages")
        public WebSocketMessage greeting(@RequestBody WebSocketMessage webSocketMessage) {
            messagingService.processMessage(webSocketMessage);
            return new WebSocketMessage(webSocketMessage.getFromUserId(), webSocketMessage.getToUserId(),
                    webSocketMessage.getMessage());
        }
}
