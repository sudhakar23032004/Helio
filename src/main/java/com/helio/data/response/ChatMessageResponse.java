package com.helio.data.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ChatMessageResponse {

    private UUID messageId;

    private UUID fromUserId;

    private UUID toUserId;

    private String message;

    private String createdBy;

    private LocalDateTime createdTimestamp;

}
