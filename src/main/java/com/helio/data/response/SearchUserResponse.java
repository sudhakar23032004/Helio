package com.helio.data.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class SearchUserResponse {

    private UUID userId;
    private String emailId;
    private String mobileNumber;
    private boolean isConnected;
    private boolean isPending;
    private boolean isAcceptancePending;

}
