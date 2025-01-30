package com.sc.authentication.service;

import com.sc.authentication.model.RefreshToken;
import com.sc.authentication.model.UserInfo;
import com.sc.authentication.repository.RefreshTokenRepository;
import com.sc.authentication.repository.UserInfoRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserInfoRepository userInfoRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public RefreshToken createRefreshToken(String userEmail) {
        UserInfo userInfo = userInfoRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken(UUID.randomUUID().toString())
            .expiresAt(Instant.now().plus(3, ChronoUnit.MONTHS))
            .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Boolean validateRefreshToken(@NonNull String refreshToken) throws RuntimeException {
        RefreshToken refreshTokenObj = findByRefreshToken(refreshToken)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Refresh token not found"));

        if (refreshTokenObj.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException(
                "Expired Token, login again to get new Token." + refreshTokenObj.getRefreshToken());
        }
        return Boolean.TRUE;
    }

    public Optional<RefreshToken> findByRefreshToken(@NonNull String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }
}