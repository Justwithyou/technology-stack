package com.technology.stack.spring.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Direct Exchange交换器配置
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/30 15:15
 */
@Configuration
public class DirectRabbitConfig {

    /**
     * 创建队列实例
     *
     * @return Queue
     */
    @Bean
    public Queue directQueue() {
        // durable表示是否持久化，默认为false
        // 持久化队列：会被存储在磁盘上，当Broker重启时仍然存在
        // 暂存队列：当前连接有效
        // exclusive默认为false只能被当前连接使用，当连接关闭后即删除，此配置优先级高于durable
        // autoDelete表示是否自动删除，当没有生产者或消费者使用此队列，该队列会自动删除
        return new Queue("directQueue", true, true, false);
    }

    /**
     * 创建交换器实例
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange", true, true);
    }

    /**
     * 绑定交换器和队列，并设置匹配键BindingKey
     *
     * @return Binding
     */
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("directRouting");
    }

    /**
     * 创建交换器
     *
     * @return DirectExchage
     */
    @Bean
    public DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange", true, true);
    }
}
