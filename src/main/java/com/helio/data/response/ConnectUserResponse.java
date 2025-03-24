package com.helio.data.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ConnectUserResponse {

    private UUID fromUserId;
    private UUID toUserId;
    private String connectionStatus;

}
