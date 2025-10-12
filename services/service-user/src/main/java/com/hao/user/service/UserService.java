package com.hao.user.service;

import com.hao.dto.UserRegisterDTO;
import com.hao.entity.User;

public interface UserService {
    void save(UserRegisterDTO userRegisterDTO);

    void sendCode(String phone);

    User getUserByPhoneWithRole(String phone, Integer role);

    User getUserByNumberWithRole(String number, Integer role);
}
