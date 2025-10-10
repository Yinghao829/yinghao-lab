package com.hao.user.service;

import com.hao.dto.UserRegisterDTO;

public interface UserService {
    void save(UserRegisterDTO userRegisterDTO);

    void sendCode(String phone);
}
