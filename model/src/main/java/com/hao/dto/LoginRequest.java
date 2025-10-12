package com.hao.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginType;
    private String phone;
    private String verifyCode;
    private String number;
    private String password;
    private Integer role;
}
