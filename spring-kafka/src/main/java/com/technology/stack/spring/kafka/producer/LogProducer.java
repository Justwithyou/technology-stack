package com.technology.stack.spring.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Properties;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/25 16:05
 */
@Slf4j
@Component
public class LogProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发布消息
     *
     * @param topic 主题
     * @param msg 消息
     */
    public void sendMessage(String topic, String msg) {

        /*
         * 此种方式为构造一个Producer生产者对象，调用send方法发送消息到Kafka集群，更加灵活，并需要自己构造一个配置对象Properties
         */
        Producer<String, String> producer = new KafkaProducer<>(new Properties());
        producer.send(new ProducerRecord<>("topic", "key", "message"), (metadata, exception) -> {
            if (exception != null) {
                log.error("发布消息失败：{}", exception.getMessage());
            }
            if (metadata != null) {
                log.info("消息追加的偏移量：{}", metadata.offset());
            }
        });

        /*
         * 以下方式是通过KafkaTemplate模板对象直接发送消息到Kafka同时监听回调
         */
        Message<String> message = MessageBuilder.withPayload(msg)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .setHeader("DataType", 1)
                        .build();
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
        // 监听回调
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("## 发布消息失败:", ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        log.info("## 发布消息成功 ...");
                    }
                });
    }
}
