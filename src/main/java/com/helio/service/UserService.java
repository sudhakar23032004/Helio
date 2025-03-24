package com.helio.service;

import com.helio.data.request.ConnectUserRequest;
import com.helio.data.request.LoginUserRequest;
import com.helio.data.response.ConnectUserResponse;
import com.helio.data.response.LoginUserResponse;
import com.helio.data.response.SearchUserResponse;
import com.helio.exception.AccessDeniedException;
import com.helio.model.UserConnection;
import com.helio.model.Users;
import com.helio.repository.UserConnectionRepository;
import com.helio.repository.UserRepository;
import com.helio.util.RSAUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.helio.data.enums.UserConnection.ACCEPTED;
import static com.helio.data.enums.UserConnection.PENDING;
import static com.helio.data.enums.UserConnection.WITHDRAWN;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserConnectionRepository userConnectionRepository;

    public UserService(UserRepository userRepository, UserConnectionRepository userConnectionRepository) {
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
    }

    public List<SearchUserResponse> getUsers(UUID loggedInUserId, String searchText) {
        List<Users> userList = userRepository.findAll();
        userList = userList.stream().filter(
                user -> !user.getUserId().equals(loggedInUserId) && user.getStatus().equals("ACTIVE"))
                .toList();
        if(!StringUtils.isEmpty(searchText)) {
            userList = userList.stream().filter(user -> user.getEmailId().contains(searchText)
              || user.getMobileNumber().toString().contains(searchText)).toList();
        }

        List<UserConnection> userConnections = userConnectionRepository.findByFromUserIdOrToUserId(loggedInUserId, loggedInUserId);
        List<UUID> connectedUUIDList = userConnections.stream().filter(user -> user.getStatus().equals(ACCEPTED.name()))
                .flatMap(x -> Stream.of(x.getFromUserId(), x.getToUserId()))
                .filter(y -> !y.equals(loggedInUserId)).toList();

        List<UUID> requestedConnectionList = userConnections.stream().filter(user ->
                user.getStatus().equals(PENDING.name()) && user.getFromUserId()
                        .equals(loggedInUserId)).map(UserConnection::getToUserId).toList();
        List<UUID> acceptConnectionList = userConnections.stream().filter(user ->
                user.getStatus().equals(PENDING.name()) && user.getToUserId()
                        .equals(loggedInUserId)).map(UserConnection::getFromUserId).toList();
        return userList.stream().map(user -> SearchUserResponse.builder().userId(user.getUserId()).emailId(user.getEmailId())
                .mobileNumber("XXXXXX".concat(user.getMobileNumber().toString().substring(6)))
                .isConnected(connectedUUIDList.contains(user.getUserId()))
                .isPending(requestedConnectionList.contains(user.getUserId()))
                .isAcceptancePending(acceptConnectionList.contains(user.getUserId()))
                .build()).toList();

    }

    public ConnectUserResponse connectUser(ConnectUserRequest connectUserRequest) {
        UserConnection userConnection = UserConnection.builder()
                .connectionId(UUID.randomUUID())
                .fromUserId(connectUserRequest.getFromUserId())
                .toUserId(connectUserRequest.getToUserId())
                .message(connectUserRequest.getMessage())
                .status(PENDING.name())
                .createdBy(connectUserRequest.getFromUserId().toString())
                .createdTimestamp(LocalDateTime.now()).build();
        UserConnection savedUserConnection = userConnectionRepository.save(userConnection);
        return ConnectUserResponse.builder().fromUserId(savedUserConnection.getFromUserId())
                .toUserId(savedUserConnection.getToUserId())
                .connectionStatus(PENDING.name()).build();
    }

    public ConnectUserResponse updateConnectionStatus(ConnectUserRequest connectUserRequest, String eventType) {
        ConnectUserResponse connectUserResponse = ConnectUserResponse.builder().build();
        if(eventType.equals(WITHDRAWN.name())) {
               UserConnection connection = userConnectionRepository.findByFromUserIdAndToUserIdAndStatus(connectUserRequest.getFromUserId(),
                       connectUserRequest.getToUserId(), PENDING.name());
               connection.setStatus(WITHDRAWN.name());
               connection.setUpdatedBy(connectUserRequest.getFromUserId().toString());
               connection.setUpdatedTimestamp(LocalDateTime.now());
               userConnectionRepository.save(connection);
               connectUserResponse.setConnectionStatus(WITHDRAWN.name());
               connectUserResponse.setFromUserId(connection.getFromUserId());
               connectUserResponse.setToUserId(connection.getToUserId());
        } else if(eventType.equals(ACCEPTED.name())) {
            UserConnection connection = userConnectionRepository.findByFromUserIdAndToUserIdAndStatus(connectUserRequest.getToUserId(),
                    connectUserRequest.getFromUserId(), PENDING.name());
            connection.setStatus(ACCEPTED.name());
            connection.setUpdatedBy(connectUserRequest.getFromUserId().toString());
            connection.setUpdatedTimestamp(LocalDateTime.now());
            userConnectionRepository.save(connection);
            connectUserResponse.setConnectionStatus(ACCEPTED.name());
            connectUserResponse.setFromUserId(connection.getToUserId());
            connectUserResponse.setToUserId(connection.getFromUserId());
        }
        return connectUserResponse;
    }
}
