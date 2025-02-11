package com.sc.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String userEmail;

    private String jwtToken;

    private String refreshToken;

    private String status;
}