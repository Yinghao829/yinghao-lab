package com.hao.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabAddDTO implements Serializable {
    private String name;
    private String location;
    private Integer capacity;
    private Integer status;
    private String description;
    private String relatedMajor;
}
