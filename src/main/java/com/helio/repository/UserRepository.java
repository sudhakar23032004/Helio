package com.helio.repository;

import com.helio.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Users findByEmailId(String emailId);

    Users findByMobileNumber(BigInteger mobileNumber);
}
