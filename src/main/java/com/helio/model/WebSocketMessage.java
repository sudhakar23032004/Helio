package com.helio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private UUID fromUserId;
    private UUID toUserId;
    private String message;
}
