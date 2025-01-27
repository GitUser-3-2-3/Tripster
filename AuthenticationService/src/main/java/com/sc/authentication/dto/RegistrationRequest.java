package com.sc.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    private String username;

    private String password;

    private String userEmail;

    private Long phoneNumber;
}