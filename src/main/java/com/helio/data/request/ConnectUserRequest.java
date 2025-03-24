package com.helio.data.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ConnectUserRequest {

    private UUID fromUserId;
    private UUID toUserId;
    private String message;
}
