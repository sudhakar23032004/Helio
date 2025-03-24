package com.helio.service;

import com.helio.data.response.ChatMessageResponse;
import com.helio.data.response.ChatMessageResponseMap;
import com.helio.data.response.SearchUserResponse;
import com.helio.model.ChatMessage;
import com.helio.model.WebSocketMessage;
import com.helio.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessagingService {

    private final ChatMessageRepository chatMessageRepository;

    public MessagingService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void processMessage(WebSocketMessage webSocketMessage) {
        ChatMessage chatMessage = ChatMessage.builder()
                .messageId(UUID.randomUUID())
                .fromUserId(webSocketMessage.getFromUserId())
                .toUserId(webSocketMessage.getToUserId())
                .message(webSocketMessage.getMessage())
                .createdBy(webSocketMessage.getFromUserId().toString())
                .createdTimestamp(LocalDateTime.now()).build();
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponseMap> loadChats(UUID loggedInUserId, UUID toUserId) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        chatMessageList.addAll(chatMessageRepository.findByFromUserIdAndToUserId(loggedInUserId, toUserId));
        chatMessageList.addAll(chatMessageRepository.findByFromUserIdAndToUserId(toUserId, loggedInUserId));
        Map<LocalDateTime, List<ChatMessage>> chatMessageResponseMap = chatMessageList.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedTimestamp))
                .peek(cMessage -> cMessage.setCreatedTimestamp(cMessage.getCreatedTimestamp()
                        .truncatedTo(ChronoUnit.DAYS))).collect(Collectors.groupingBy(ChatMessage::getCreatedTimestamp));
        final List<ChatMessageResponseMap> chatMessageResponseMapList = new ArrayList<>();
        chatMessageResponseMap.forEach((key, value) -> chatMessageResponseMapList.add(ChatMessageResponseMap.builder()
                .createdTimestamp(key)
                .chatMessageResponseList(transformChatMessage(value)).build()));
        return chatMessageResponseMapList.stream().sorted(Comparator.comparing(ChatMessageResponseMap::getCreatedTimestamp)).collect(Collectors.toList());
    }

    private List<ChatMessageResponse> transformChatMessage(List<ChatMessage> chatMessageList) {
        return chatMessageList.stream().map(chatMessage -> ChatMessageResponse.builder()
                .messageId(chatMessage.getMessageId())
                .fromUserId(chatMessage.getFromUserId())
                .toUserId(chatMessage.getToUserId())
                .message(chatMessage.getMessage())
                .createdBy(chatMessage.getCreatedBy())
                .build()).toList();
    }
}
