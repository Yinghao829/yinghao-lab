package com.hao.lab.controller;

import com.hao.common.result.PageResult;
import com.hao.common.result.Result;
import com.hao.dto.LabAddDTO;
import com.hao.dto.LabPageQueryDTO;
import com.hao.entity.Lab;
import com.hao.lab.service.LabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lab")
@Slf4j
public class LabController {

    @Autowired
    private LabService labService;

    @PostMapping("/teacher/add")
    public Result addLab(@RequestBody LabAddDTO labAddDTO) {
        log.info("新增实验室：{}", labAddDTO);
        labService.addLab(labAddDTO);
        return Result.success();
    }

    @GetMapping("/shared/labList")
    public Result<PageResult> labPageQuery(LabPageQueryDTO labPageQueryDTO) {
        log.info("分页查询：{}", labPageQueryDTO);
        PageResult pageResult = labService.pageQuery(labPageQueryDTO);
        return Result.success(pageResult);
    }
}
