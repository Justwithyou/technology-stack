package com.technology.stack.sprin.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2021/1/4 19:04
 */
public class Consumer {

    public static void main(String[] args) throws Exception {

        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("push-consumer-group");
        // 设置NamesrvAddr的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅一个或多个Topic以及Tag来过滤需要消费的消息，*表示Topic下的所有消息
        consumer.subscribe("sync-rocketmq-topic", "*");
        // 注册回调实现类来处理从Broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext context) {
                System.out.printf("%s 接收到新的消息：%s %n", Thread.currentThread().getName(), msgList);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("消费者启动");
    }

    /**
     * 顺序消息消费，可带事务（可控制Offset何时提交）
     *
     * @throws Exception MQClientException
     */
    public void orderConsumer() throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order-consumer");
        consumer.setNamesrvAddr("localhost:9876");
        // 设置Consumer第一次启动时是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，则按照上一次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("order-topic", "TagA || TagB || TagC");

        // 注册监听消息回调
        // 此处可以采用Lambda表达式的写法
        consumer.registerMessageListener(new MessageListenerOrderly() {

            final Random random = new Random();
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgList, ConsumeOrderlyContext context) {

                // 设置自动提交
                context.setAutoCommit(true);
                for (MessageExt msg : msgList) {
                    // 查看每个Queue是否有唯一的Consumer线程来消费，订单对每个Queue有序
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "queueId=" +
                            msg.getQueueId() + ", content:" + new String(msg.getBody()));
                }

                // 业务逻辑
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println("消费者启动");
    }

    /**
     * 消费延时消息
     *
     * @throws Exception MQClientException
     */
    public void delayConsumer() throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delay-consumer");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.subscribe("delay-topic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgList) {
                    System.out.println("Receive message[msgId=" + msg.getMsgId() + "] " +
                            (System.currentTimeMillis() - msg.getStoreTimestamp()) + "ms later");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
