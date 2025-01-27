package com.sc.authentication.repository;

import com.sc.authentication.model.UserInfo;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    Optional<UserInfo> findByUserId(String userId);

    Optional<UserInfo> findByUserEmail(@Email(message = "Invalid Email.") String userEmail);
}