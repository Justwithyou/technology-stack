package com.technology.stack.spring.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/25 16:05
 */
@Slf4j
@Component
public class LogConsumer {

    @KafkaListener(
            groupId = "SpringBoot",
            topics = "spring-kafka")
    public void consumeAlarm(
            @Header("DataType") Integer dataType,
            ConsumerRecord<?, ?> record,
            Acknowledgment acknowledgment) {
        try {
            log.info(
                    MessageFormat.format(
                            "收到存储信息:topic:{0},offset:{1},存储内容:{2},分区为{3}!",
                            record.topic(), record.offset(), record.value(), record.partition()));
            // TODO:做相应信息处理
            // 提交确认，回执
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(MessageFormat.format("处理kafka信息数据发生异常:{0}", e.getMessage()), e);
            acknowledgment.acknowledge();
        }

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(new Properties());
        kafkaConsumer.subscribe(Collections.emptyList());
    }

    /**
     * 自定义消费方法，不使用监听的方式
     */
    public void consumer() {

        Consumer<String, String> consumer = new KafkaConsumer<>(new Properties());
        consumer.subscribe(Collections.singletonList("topic"));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(10));

        for (TopicPartition topicPartition : consumerRecords.partitions()) {
            List<ConsumerRecord<String, String>> consumerRecordList = consumerRecords.records(topicPartition);
            for (ConsumerRecord<String, String> consumerRecord : consumerRecordList) {
                if (consumerRecord != null) {
                    log.info("消费到的消息，topic：{}，partition：{}，key：{}，value：{}", consumerRecord.topic(),
                            consumerRecord.partition(), consumerRecord.key(), consumerRecord.value());
                }
            }
        }
        try {
            // 手动提交偏移量，异步提交
            consumer.commitAsync();
        } catch (Exception e) {
            // 手动提交偏移量，同步提交
            consumer.commitSync();
        }
    }
}
