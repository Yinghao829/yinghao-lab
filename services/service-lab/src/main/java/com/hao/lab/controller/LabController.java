package com.hao.lab.controller;

import com.hao.common.constant.LabConstant;
import com.hao.common.constant.MqConstant;
import com.hao.common.result.PageResult;
import com.hao.common.result.Result;
import com.hao.dto.LabAddDTO;
import com.hao.dto.LabPageQueryDTO;
import com.hao.dto.LabUpdateDTO;
import com.hao.lab.service.LabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/lab")
@Slf4j
public class LabController {


    @Autowired
    private LabService labService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/teacher/add")
    public Result addLab(@RequestBody LabAddDTO labAddDTO) {
        log.info("新增实验室：{}", labAddDTO);
        labService.addLab(labAddDTO);
        String prefix = LabConstant.LAB_QUERY_REDIS_KEY + ":page:*";
        Set<String> keys = redisTemplate.keys(prefix);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        return Result.success();
    }

    @GetMapping("/shared/labList")
    public Result<PageResult> labPageQuery(LabPageQueryDTO labPageQueryDTO) {
        log.info("分页查询：{}", labPageQueryDTO);
        PageResult pageResult = new PageResult();
        // 将分页查询的结果保存到redis中，如果添加查询条件，则之间查数据库
        // 判断有无查询条件
        if (labPageQueryDTO.getStartTime() != null ||
            labPageQueryDTO.getEndTime() != null ||
                !Objects.equals(labPageQueryDTO.getRelatedMajor(), "") ||
                !Objects.equals(labPageQueryDTO.getName(), "") ||
            labPageQueryDTO.getReservationStatus() != null) {
            // 有查询条件，直接查数据库返回数据
            pageResult = labService.pageQuery(labPageQueryDTO);
            return Result.success(pageResult);
        }
        // 如果直接分页查询，将结果保存到redis中
        // 构造key，页码
        String key = LabConstant.LAB_QUERY_REDIS_KEY + ":page:" + labPageQueryDTO.getPage();
        pageResult = (PageResult) redisTemplate.opsForValue().get(key);
        if (pageResult == null) {
            pageResult = labService.pageQuery(labPageQueryDTO);
            redisTemplate.opsForValue().set(key, pageResult);
        }
        return Result.success(pageResult);
    }

    @Transactional
    @PutMapping("/teacher/update")
    public Result updateLab(@RequestBody LabUpdateDTO labUpdateDTO) {
        log.info("更新实验室信息：{}", labUpdateDTO);
        String key = LabConstant.LAB_QUERY_REDIS_KEY + ":page:" + labUpdateDTO.getPage();
        log.info("更新操作，第一次删缓存：{}",labUpdateDTO);
        redisTemplate.delete(key);
        // 更新
        labService.updateLab(labUpdateDTO);
        // 发送延时消息 删缓存
        log.info("发布消息");
        rabbitTemplate.convertAndSend(MqConstant.LAB_DELAY_EXCHANGE, MqConstant.LAB_DELAY_ROUTING_KEY, labUpdateDTO, new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelayLong(1000L);
                return message;
            }
        });
        return Result.success();
    }

    @Transactional
    @PostMapping("/teacher/delete")
    public Result deleteLab(@RequestParam Long id, @RequestParam Integer page) {
        log.info("删除实验室id：{}", id);
        // 先删缓存
        String key = LabConstant.LAB_QUERY_REDIS_KEY + ":page:" + page;
        redisTemplate.delete(key);
        // 删除
        labService.deleteLab(id);
        // 延时删缓存
        LabUpdateDTO labUpdateDTO = new LabUpdateDTO();
        labUpdateDTO.setId(id);
        labUpdateDTO.setPage(page);
        rabbitTemplate.convertAndSend(MqConstant.LAB_DELAY_EXCHANGE, MqConstant.LAB_DELAY_ROUTING_KEY, labUpdateDTO, message -> {
            message.getMessageProperties().setDelayLong(1000L);
            return message;
        });
        return Result.success();
    }
}
