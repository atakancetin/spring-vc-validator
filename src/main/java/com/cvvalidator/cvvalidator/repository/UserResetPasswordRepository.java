package com.cvvalidator.cvvalidator.repository;

import java.util.Optional;

import com.cvvalidator.cvvalidator.model.UserResetPasswordToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResetPasswordRepository extends JpaRepository<UserResetPasswordToken, Long> {
    Optional<UserResetPasswordToken> findByToken(String token);
}