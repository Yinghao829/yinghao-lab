package com.hao.lab.service;

import com.hao.common.result.PageResult;
import com.hao.dto.LabAddDTO;
import com.hao.dto.LabPageQueryDTO;

public interface LabService {
    void addLab(LabAddDTO labAddDTO);

    PageResult pageQuery(LabPageQueryDTO labPageQueryDTO);
}
