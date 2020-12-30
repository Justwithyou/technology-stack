package com.technology.stack.spring.rabbitmq.consumer;

import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ消费者
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/30 15:44
 */
public class RabbitConsumer {

    /**
     * 监听
     *
     * @param message 消息
     */
    @RabbitHandler
    @RabbitListener(queues = "directQueue")
    public void process(Map<String, String> message) {

        System.out.println("消费者接收的消息：" + message.toString());
    }

    public void consumer() throws IOException, TimeoutException {

        // 获取连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();

        // 创建信道
        Channel channel = connection.createChannel();
        channel.queueDeclare("directQueue", true, true, false, null);
        channel.queueBind("directQueue", "directExchange", "directRouting");

        // 消费逻辑
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("消费者接收消息：" + message);
            }
        };
        // 监听
        channel.basicConsume("directQueue", consumer);
    }
}
