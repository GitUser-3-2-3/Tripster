package com.sc.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "Invalid Email.")
    private String userEmail;

    @NotEmpty(message = "Password cannot be blank.")
    private String password;

    @Size(max = 10, message = "Phone Number cannot exceed 10 digits.")
    private Long phoneNumber;
}