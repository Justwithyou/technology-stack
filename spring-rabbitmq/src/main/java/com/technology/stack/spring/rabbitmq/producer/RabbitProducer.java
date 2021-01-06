package com.technology.stack.spring.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * RabbitMQ生产者
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/30 15:35
 */
public class RabbitProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMessage() {

        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message,hello";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, String> map = new HashMap<>(8);
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend("directExchange", "directRouting", map);
    }

}
