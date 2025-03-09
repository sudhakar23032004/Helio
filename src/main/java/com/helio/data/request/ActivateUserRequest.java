package com.helio.data.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ActivateUserRequest {

    private UUID userId;
    private String password;

}
