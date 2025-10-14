package com.hao.reservation.mapper;

import com.hao.dto.ReservationInfoDTO;
import com.hao.vo.ReservationStatusVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<ReservationStatusVO> getLabStatus(ReservationInfoDTO reservationInfoDTO);
}
