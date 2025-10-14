package com.hao.reservation.reservation.service.impl;

import com.hao.dto.ReservationInfoDTO;
import com.hao.reservation.mapper.ReservationMapper;
import com.hao.reservation.reservation.service.ReservationService;
import com.hao.vo.ReservationStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<ReservationStatusVO> getStatusByIds(ReservationInfoDTO reservationInfoDTO) {
        List<ReservationStatusVO> labStatusList = reservationMapper.getLabStatus(reservationInfoDTO);
        return labStatusList;
    }
}
