package com.hao.user.service;

import com.hao.dto.LoginRequest;
import com.hao.entity.User;

public interface AuthenticationStrategy {
    User authenticate(LoginRequest request);
}
