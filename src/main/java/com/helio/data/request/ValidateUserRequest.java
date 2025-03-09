package com.helio.data.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ValidateUserRequest {

    private UUID userId;
    private String textMessageId;
    private String emailMessageId;
    private Integer otp;
}
