package com.hao.vo;

import lombok.Data;

@Data
public class ReservationStatusVO {
    // 实验室id
    private Long labId;
    // 预约状态
    private Integer reservationStatus;
}
