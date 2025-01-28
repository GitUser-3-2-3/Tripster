package com.sc.authentication.service;

import com.sc.authentication.model.UserInfo;
import com.sc.authentication.repository.UserInfoRepository;
import com.sc.authentication.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with userEmail: " + userEmail));
        return new CustomUserDetails(userInfo);
    }
}