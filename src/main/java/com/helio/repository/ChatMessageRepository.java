package com.helio.repository;

import com.helio.model.ChatMessage;
import com.helio.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findByFromUserIdAndToUserId(UUID loggedInUserId, UUID toUserId);
}
