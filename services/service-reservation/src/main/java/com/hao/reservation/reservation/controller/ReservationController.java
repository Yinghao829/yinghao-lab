package com.hao.reservation.reservation.controller;

import com.hao.dto.ReservationInfoDTO;
import com.hao.reservation.reservation.service.ReservationService;
import com.hao.vo.ReservationStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@Slf4j
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/getReservationStatus")
    public List<ReservationStatusVO> getReservationStatus(@RequestBody ReservationInfoDTO reservationInfoDTO){
        log.info("远程调用：实验室id：{}", reservationInfoDTO);
        // 根据实验室id查询实验室预约状态
        List<ReservationStatusVO> list = reservationService.getStatusByIds(reservationInfoDTO);
        return list;
    }
}
