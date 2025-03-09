package com.helio.repository;

import com.helio.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, UUID> {

    UserRegistration findByMobileNumberOrEmailId(Long mobileNumber, String emailId);

    UserRegistration findByUserIdAndTwilioMessageIdAndEmailId(UUID userId, String twilioMessageId, String emailId);
}
