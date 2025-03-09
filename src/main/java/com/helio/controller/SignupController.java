package com.helio.controller;


import com.helio.data.request.ActivateUserRequest;
import com.helio.data.request.RegisterUserRequest;
import com.helio.data.request.ValidateUserRequest;
import com.helio.data.response.RegisterUserResponse;
import com.helio.data.response.ValidateUserResponse;
import com.helio.service.SignupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-registration")
@AllArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping(value = "sign-up")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(signupService.registerUser(registerUserRequest));
    }

    @PostMapping(value = "validate")
    public ResponseEntity<ValidateUserResponse> validateUser(@RequestBody ValidateUserRequest validateUserRequest) {
        return ResponseEntity.ok(signupService.validateUser(validateUserRequest));
    }

    @PostMapping(value = "activate")
    public ResponseEntity<String> activateUser(@RequestBody ActivateUserRequest activateUserRequest) {
        signupService.activateUser(activateUserRequest);
        return ResponseEntity.ok("User activated");
    }


}