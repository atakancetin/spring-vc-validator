package com.cvvalidator.cvvalidator.manager;

import java.util.Calendar;
import java.util.UUID;

import com.cvvalidator.cvvalidator.model.User;
import com.cvvalidator.cvvalidator.model.UserResetPasswordToken;
import com.cvvalidator.cvvalidator.repository.UserRepository;
import com.cvvalidator.cvvalidator.repository.UserResetPasswordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ApiAuthenticationManager implements AuthenticationManager {

    @Autowired
    UserResetPasswordRepository tokenRepository; 
   

    private AuthenticationManager defaultManager;

    @Value("${app.resetPasswordTokenExpirationInDay:1}")
    private int resetPasswordTokenExpirationInDay;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ApiAuthenticationManager(AuthenticationManager defaultManager) {
        this.defaultManager = defaultManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return defaultManager.authenticate(authentication);
    }

    public String generateResetPasswordToken(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, resetPasswordTokenExpirationInDay);
        String token = UUID.randomUUID().toString();
        UserResetPasswordToken userResetPasswordToken;
        if (user.getUserResetPasswordToken() != null) {
            userResetPasswordToken = user.getUserResetPasswordToken();
            userResetPasswordToken.setExpiration(calendar.getTime());
            userResetPasswordToken.setToken(token);
        } else {
            userResetPasswordToken = new UserResetPasswordToken(token, calendar.getTime(), user);
        }
        tokenRepository.save(userResetPasswordToken);
        return userResetPasswordToken.getToken();
    }

    public Boolean validateResetPasswordToken(String token) {
        UserResetPasswordToken tokenHolder = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("GEçersiz Token"));
        if (tokenHolder.getToken().equals(token)) {
            Calendar calendar = Calendar.getInstance();

            if (tokenHolder.getExpiration().getTime() - calendar.getTime().getTime() <= 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String generateResetPasswordToken(String userEmail) {
        User user = getUser(userEmail);
        return generateResetPasswordToken(user);
    }

    public Boolean changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    public Boolean changePassword(String usernameOrEmail, String newPassword) {
        User user = getUser(usernameOrEmail);
        return changePassword(user, newPassword);
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email : " + userEmail));
    }
}