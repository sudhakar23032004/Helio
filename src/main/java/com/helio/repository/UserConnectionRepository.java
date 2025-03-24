package com.helio.repository;

import com.helio.model.UserConnection;
import com.helio.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID> {

    List<UserConnection> findByFromUserIdOrToUserId(UUID fromUserId, UUID toUserId);

    UserConnection findByFromUserIdAndToUserIdAndStatus(UUID userId1, UUID userId2, String status);
}
