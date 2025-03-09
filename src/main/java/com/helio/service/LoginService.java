package com.helio.service;

import com.helio.data.request.LoginUserRequest;
import com.helio.data.response.LoginUserResponse;
import com.helio.exception.AccessDeniedException;
import com.helio.model.Users;
import com.helio.repository.UserRepository;
import com.helio.util.RSAUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.Objects;

@Service
public class LoginService {

    private final UserRepository userRepository;

    private final RSAUtil rsaUtil;


    public LoginService(UserRepository userRepository, RSAUtil rsaUtil) {
        this.userRepository = userRepository;
        this.rsaUtil = rsaUtil;
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        if(StringUtils.isEmpty(loginUserRequest.getUsername()) ||
                StringUtils.isEmpty(loginUserRequest.getPassword())) {
            throw new AccessDeniedException("Username or password is empty");
        } else {
            Users user;
            if(loginUserRequest.getUsername().contains("@")) {
                 user = userRepository.findByEmailId(loginUserRequest.getUsername());
            } else {
                 user = userRepository.findByMobileNumber(BigInteger.valueOf(Long.
                         parseLong(loginUserRequest.getUsername())));
            }
            if(Objects.nonNull(user)) {
                String password = rsaUtil.decryptPassword(user.getPassword());
                if(password.equals(loginUserRequest.getPassword())) {
                   return LoginUserResponse.builder()
                           .userId(user.getUserId())
                           .emailId(user.getEmailId()).build();
                } else {
                    throw new AccessDeniedException("Wrong password");
                }
            } else {
                throw new AccessDeniedException("Wrong username or password");
            }
        }
    }
}
