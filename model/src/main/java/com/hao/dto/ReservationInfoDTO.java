package com.hao.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationInfoDTO {
    // 实验室id
    private List<Long> idList;
    // 预约起始时间
    private LocalDateTime startTime;
    // 预约结束时间
    private LocalDateTime endTime;
    // 当前时间
    private LocalDateTime currentTime;
}
