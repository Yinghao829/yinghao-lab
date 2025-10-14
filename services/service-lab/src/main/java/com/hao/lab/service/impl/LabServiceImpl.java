package com.hao.lab.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hao.common.result.PageResult;
import com.hao.dto.LabAddDTO;
import com.hao.dto.LabPageQueryDTO;
import com.hao.dto.ReservationInfoDTO;
import com.hao.entity.Lab;
import com.hao.lab.feign.ReservationFeignClient;
import com.hao.lab.mapper.LabMapper;
import com.hao.lab.service.LabService;
import com.hao.vo.LabVO;
import com.hao.vo.ReservationStatusVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LabServiceImpl implements LabService {

    @Autowired
    private LabMapper labMapper;
    @Autowired
    private ReservationFeignClient reservationFeignClient;

    public void addLab(LabAddDTO labAddDTO) {
        Lab lab = new Lab();
        BeanUtils.copyProperties(labAddDTO, lab);
        labMapper.add(lab);
    }

    @Override
    public PageResult pageQuery(LabPageQueryDTO labPageQueryDTO) {
        PageHelper.startPage(labPageQueryDTO.getPage(), labPageQueryDTO.getPageSize());
        Page<LabVO> page = labMapper.pageQuery(labPageQueryDTO);
        long total = page.getTotal();
        List<LabVO> list = page.getResult();
        List<Long> labIds = new ArrayList<>();
        // 通过远程调用查询预约表，返回这一页实验室的预约状态
        labIds = list.stream()
                .map(LabVO::getId)
                .collect(Collectors.toList());
        ReservationInfoDTO resInfoDTO = ReservationInfoDTO.builder()
                .idList(labIds)
                .currentTime(labPageQueryDTO.getCurrentTime())
                .startTime(labPageQueryDTO.getStartTime())
                .endTime(labPageQueryDTO.getEndTime())
                .build();
        List<ReservationStatusVO> reservationStatusVOList = reservationFeignClient.getReservationStatus(resInfoDTO);
        Map<Long, Integer> statusMap = reservationStatusVOList.stream()
                .collect(Collectors.toMap(
                        ReservationStatusVO::getLabId,
                        ReservationStatusVO::getReservationStatus
                ));
        list.forEach(labVO -> {
            Integer status = statusMap.get(labVO.getId());
            labVO.setReservationStatus(status);
        });
        return new PageResult(total, list);
    }
}
