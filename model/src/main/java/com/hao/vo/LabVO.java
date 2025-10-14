package com.hao.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LabVO implements Serializable {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private Integer status;
    private String description;
    private LocalDateTime updateTime;
    private Integer reservationStatus;
    private String relatedMajor;
}
