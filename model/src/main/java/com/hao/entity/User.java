package com.hao.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private Long id;
    // 学号或教师号
    private String number;

    private String password;
    private String realName;
    private Integer role;

    private String phone;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
