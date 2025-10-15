package com.hao.lab.listener;

import com.hao.common.constant.LabConstant;
import com.hao.common.constant.MessageConstant;
import com.hao.common.constant.MqConstant;
import com.hao.dto.LabUpdateDTO;
import com.hao.lab.controller.LabController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LabUpdateListener {

    @Autowired
    private RedisTemplate redisTemplate;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConstant.LAB_DELAY_QUEUE),
            exchange = @Exchange(value = MqConstant.LAB_DELAY_EXCHANGE, delayed = "true", type = ExchangeTypes.TOPIC),
            key = MqConstant.LAB_DELAY_ROUTING_KEY
    ))
    public void listenLabUpdateDelayMessage(LabUpdateDTO labUpdateDTO) {
        log.info("执行延时删除：{}", labUpdateDTO);
        // 延时后删缓存
        String key = LabConstant.LAB_QUERY_REDIS_KEY + ":page:" + labUpdateDTO.getPage();
        redisTemplate.delete(key);
    }
}
