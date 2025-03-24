package com.helio.controller;

import com.helio.data.request.ConnectUserRequest;
import com.helio.data.response.ChatMessageResponseMap;
import com.helio.data.response.ConnectUserResponse;
import com.helio.data.response.SearchUserResponse;
import com.helio.service.MessagingService;
import com.helio.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/chat")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessagingService messagingService;

    @GetMapping
    public ResponseEntity<List<ChatMessageResponseMap>> loadChats(@RequestParam UUID loggedInUserId,
                                                                  @RequestParam UUID toUserId) {
        return ResponseEntity.ok(messagingService.loadChats(loggedInUserId, toUserId));
    }


}
