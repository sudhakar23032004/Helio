package com.helio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "chat_messages", schema = "helio")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    private UUID messageId;

    private UUID fromUserId;

    private UUID toUserId;

    private String message;

    private String createdBy;

    private LocalDateTime createdTimestamp;

}
