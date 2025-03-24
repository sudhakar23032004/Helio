package com.helio.controller;

import com.helio.data.request.ConnectUserRequest;
import com.helio.data.request.LoginUserRequest;
import com.helio.data.response.ConnectUserResponse;
import com.helio.data.response.LoginUserResponse;
import com.helio.data.response.SearchUserResponse;
import com.helio.service.LoginService;
import com.helio.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<SearchUserResponse>> getUsers(@RequestParam UUID loggedInUserId,
                                                             @RequestParam(required = false) String searchText) {
        return ResponseEntity.ok(userService.getUsers(loggedInUserId, searchText));
    }

    @PostMapping(value = "connect")
    public ResponseEntity<ConnectUserResponse> connectUser(@RequestBody ConnectUserRequest connectUserRequest) {
        return ResponseEntity.ok(userService.connectUser(connectUserRequest));
    }

    @PutMapping(value = "/update-connection-status")
    public ResponseEntity<ConnectUserResponse> updateConnectionStatus(@RequestBody ConnectUserRequest connectUserRequest,
                                                                      @RequestParam String eventType) {
        return ResponseEntity.ok(userService.updateConnectionStatus(connectUserRequest, eventType));
    }


}
