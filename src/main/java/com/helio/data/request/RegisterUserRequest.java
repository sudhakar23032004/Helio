package com.helio.data.request;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String emailId;
    private Long mobileNumber;
}
