package com.hao.reservation.reservation.service;

import com.hao.dto.ReservationInfoDTO;
import com.hao.vo.ReservationStatusVO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationStatusVO> getStatusByIds(ReservationInfoDTO reservationInfoDTO);
}
