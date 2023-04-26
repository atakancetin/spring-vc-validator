package com.cvvalidator.cvvalidator.payload;

public class JwtAuthenticationData {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationData(String accessToken) {
        this.accessToken = accessToken;
    }
}