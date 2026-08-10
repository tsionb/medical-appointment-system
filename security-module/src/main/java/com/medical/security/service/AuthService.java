package com.medical.security.service;

import com.medical.security.dto.request.LoginRequest;
import com.medical.security.dto.request.RegisterRequest;
import com.medical.security.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
