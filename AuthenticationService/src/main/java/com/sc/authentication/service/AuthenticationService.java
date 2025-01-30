package com.sc.authentication.service;

import com.sc.authentication.dto.LoginRequest;
import com.sc.authentication.dto.LoginResponse;
import com.sc.authentication.dto.RegistrationRequest;
import com.sc.authentication.dto.RegistrationResponse;
import com.sc.authentication.jwt.JwtService;
import com.sc.authentication.model.Roles;
import com.sc.authentication.model.UserInfo;
import com.sc.authentication.repository.RefreshTokenRepository;
import com.sc.authentication.repository.RoleRepository;
import com.sc.authentication.repository.UserInfoRepository;
import com.sc.authentication.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public RegistrationResponse registerUser(RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Boolean userExists = userInfoRepository.existsByUserEmail(request.getUserEmail());
        if (userExists) return RegistrationResponse.builder()
            .userEmail(request.getUserEmail()).status("User already exists.").build();

        Roles defaultRole = roleRepository.findByRoleName("ROLE_USER")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not initialized."));

        UserInfo userInfo = UserInfo.builder().username(request.getUsername())
            .password(request.getPassword()).userEmail(request.getUserEmail())
            .phoneNumber(request.getPhoneNumber()).userRoles(Set.of(defaultRole))
            .build();
        UserInfo savedUser = userInfoRepository.save(userInfo);

        return RegistrationResponse.builder().username(savedUser.getUsername())
            .userEmail(savedUser.getUserEmail()).status("Registration Successful").build();
    }

    public LoginResponse userLogin(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUserEmail(), request.getPassword())
        );
        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed.");
        }
        var extraClaims = new HashMap<String, Object>();
        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("USER_DETAILS::{}", userDetails);

        extraClaims.put("username", userDetails.getUsername());
        var jwtToken = jwtService.generateToken(extraClaims, userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return LoginResponse.builder().userEmail(userDetails.getUsername()).jwtToken(jwtToken)
            .refreshToken(refreshToken.getRefreshToken())
            .status("Success").build();
    }
}