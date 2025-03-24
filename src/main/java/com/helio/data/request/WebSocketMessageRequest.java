package com.helio.data.request;

import lombok.Data;

import java.util.UUID;

@Data
public class WebSocketMessageRequest {

    private UUID userId;
    private String message;
}
