package com.helio.data.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RegisterUserResponse {

    private UUID userId;
    private String textMessageId;
    private String emailMessageId;

}
