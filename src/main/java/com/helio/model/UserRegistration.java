package com.helio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "user_invite", schema = "helio")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {

    @Id
    private UUID userId;

    private String emailId;

    private Long mobileNumber;

    private String twilioMessageId;

    private String emailMessageId;

    private Integer otp;

    private String status;

    private String createdBy;

    private LocalDateTime createdTimestamp;

    private String updatedBy;

    private LocalDateTime updatedTimestamp;

}
