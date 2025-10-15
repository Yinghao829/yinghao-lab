package com.hao.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabUpdateDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Integer capacity;
    private Integer status;
    private String relatedMajor;
    private int page;
}
