package com.helio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "users", schema = "helio")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    private UUID userId;

    private byte[] password;

    private String emailId;

    private BigInteger mobileNumber;

    private String status;

    private String createdBy;

    private LocalDateTime createdTimestamp;

    private String updatedBy;

    private LocalDateTime updatedTimestamp;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserRegistration userRegistration;

}
