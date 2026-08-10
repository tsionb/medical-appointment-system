package com.medical.security.dto.response;

import com.medical.common.enums.Role;


public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Role role;
    private Long userId;
    private String email;


    private Long profileId;

    public AuthResponse() {}

    public AuthResponse(String token, Role role, Long userId,
                        String email, Long profileId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.email = email;
        this.profileId = profileId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
}
