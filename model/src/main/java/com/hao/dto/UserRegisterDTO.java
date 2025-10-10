package com.hao.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserRegisterDTO implements Serializable {

    private String number;
    private String password;
    private Integer role;
    private String verifyCode;
    private String phone;
}
