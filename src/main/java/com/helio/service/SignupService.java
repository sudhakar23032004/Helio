package com.helio.service;

import com.helio.adapter.MessageAdapter;
import com.helio.data.enums.UserStatus;
import com.helio.data.request.ActivateUserRequest;
import com.helio.data.request.EmailRequest;
import com.helio.data.request.RegisterUserRequest;
import com.helio.data.request.ValidateUserRequest;
import com.helio.data.response.RegisterUserResponse;
import com.helio.data.response.ValidateUserResponse;
import com.helio.exception.AccountActivatedException;
import com.helio.exception.InvalidUserException;
import com.helio.exception.UserRegistrationException;
import com.helio.model.UserRegistration;
import com.helio.model.Users;
import com.helio.repository.UserRegistrationRepository;
import com.helio.repository.UserRepository;
import com.helio.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.helio.data.enums.UserInviteStatus.ACTIVATED;
import static com.helio.data.enums.UserInviteStatus.INVITED;
import static com.helio.data.enums.UserInviteStatus.VALIDATED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class SignupService {

    private final MessageAdapter messageAdapter;

    private final UserRegistrationRepository userRegistrationRepository;

    private final UserRepository userRepository;

    private final RSAUtil rsaUtil;

    @Value("${application-id}")
    private UUID applicationId;

    public SignupService(MessageAdapter messageAdapter, UserRegistrationRepository userRegistrationRepository,
                         UserRepository userRepository,
                         RSAUtil rsaUtil) {
        this.messageAdapter = messageAdapter;
        this.userRegistrationRepository = userRegistrationRepository;
        this.userRepository = userRepository;
        this.rsaUtil = rsaUtil;
    }

    public RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest) {
        UserRegistration userRegistrationOptional = userRegistrationRepository
                .findByMobileNumberOrEmailId(registerUserRequest.getMobileNumber(), registerUserRequest.getEmailId());
        if (nonNull(userRegistrationOptional)) {
            throw new UserRegistrationException("UserRegistration already exists");
        }
        UUID userId = UUID.randomUUID();
        Random random = new Random();
        int number = random.nextInt(999999);
        String otp = String.format("%06d", number);
        String message = """
                Hello !
                  Welcome to Helio
                  Your OTP for signup - %s
                """.formatted(otp);
        String textMessageId = messageAdapter.sendTextMessage(registerUserRequest.getMobileNumber(), message);
        String emailMessageId = messageAdapter.sendEmail(EmailRequest.builder()
                .subject("Helio Sign up").recipient(registerUserRequest.getEmailId()).msgBody(message).build());
        userRegistrationRepository.save(UserRegistration.builder()
                .userId(userId)
                .emailId(registerUserRequest.getEmailId())
                .mobileNumber(registerUserRequest.getMobileNumber())
                .twilioMessageId(textMessageId)
                .emailMessageId(emailMessageId)
                .otp(Integer.valueOf(otp))
                .status(INVITED.name())
                .createdBy(applicationId.toString())
                .createdTimestamp(LocalDateTime.now())
                .updatedBy(null)
                .updatedTimestamp(null).build());
        return RegisterUserResponse.builder().userId(userId)
                .textMessageId(textMessageId).emailMessageId(emailMessageId).build();
    }

    public ValidateUserResponse validateUser(ValidateUserRequest validateUserRequest) {
        Optional<UserRegistration> userRegistrationOptional = userRegistrationRepository.findById(validateUserRequest.getUserId());
        UserRegistration userRegistration = userRegistrationOptional.orElse(null);
        if(nonNull(userRegistration) && userRegistration.getTwilioMessageId().equals(validateUserRequest.getTextMessageId())
                && userRegistration.getEmailMessageId().equals(validateUserRequest.getEmailMessageId())
                && userRegistration.getOtp().equals(validateUserRequest.getOtp())) {
            userRegistration.setUpdatedBy(validateUserRequest.getUserId().toString());
            userRegistration.setUpdatedTimestamp(LocalDateTime.now());
            userRegistration.setStatus(VALIDATED.name());
            userRegistrationRepository.save(userRegistration);
            return ValidateUserResponse.builder().userId(validateUserRequest.getUserId())
                    .authenticated(true).build();
        } else {
            throw new InvalidUserException("OTP is not valid");
        }
    }

    public void activateUser(ActivateUserRequest activateUserRequest) {
        if(isNull(activateUserRequest.getUserId()) || isNull(activateUserRequest.getPassword())
                || activateUserRequest.getPassword().isBlank()) {
            throw new InvalidUserException("User Id or password field can't empty");
        }
        Optional<UserRegistration> userRegistrationOptional = userRegistrationRepository.findById(activateUserRequest.getUserId());
        UserRegistration userRegistration = userRegistrationOptional.orElse(null);
        if(isNull(userRegistration)) {
            throw new InvalidUserException("User not found in the system");
        } else if(userRegistration.getStatus().equals(ACTIVATED.name())) {
            throw new AccountActivatedException("User account already activated");
        } else {
            byte[] encryptedPassword = rsaUtil.encryptPassword(activateUserRequest.getPassword());
            userRegistration.setUpdatedBy(activateUserRequest.getUserId().toString());
            userRegistration.setUpdatedTimestamp(LocalDateTime.now());
            userRegistration.setStatus(ACTIVATED.name());
            userRegistrationRepository.save(userRegistration);
            Users user = Users.builder()
                    .userId(userRegistration.getUserId())
                    .password(encryptedPassword)
                    .emailId(userRegistration.getEmailId())
                    .mobileNumber(BigInteger.valueOf(Long
                                    .valueOf(userRegistration.getMobileNumber())))
                    .status(UserStatus.ACTIVE.name())
                    .createdBy(userRegistration.getUserId().toString())
                    .createdTimestamp(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
    }
}