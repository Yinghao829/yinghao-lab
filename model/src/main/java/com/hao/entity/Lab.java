package com.hao.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Lab implements Serializable {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private Integer status;
    private String description;
    private String relatedMajor;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
