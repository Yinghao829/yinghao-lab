package com.hao.lab.feign;

import com.hao.dto.ReservationInfoDTO;
import com.hao.vo.ReservationStatusVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-reservation", path = "/api/reservation")
public interface ReservationFeignClient {
    /**
     *  根据实验室id查询该实验室是否被使用中
     */
    @PostMapping("/getReservationStatus")
    List<ReservationStatusVO> getReservationStatus(@RequestBody ReservationInfoDTO reservationInfoDTO);
}
