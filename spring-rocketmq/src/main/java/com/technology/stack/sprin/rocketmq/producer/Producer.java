package com.technology.stack.sprin.rocketmq.producer;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2021/1/4 17:14
 */
public class Producer {

    public static void main(String[] args) {

    }

    /**
     * 发送同步消息，可靠性较高
     *
     * @throws Exception MQClientException
     */
    public void syncSendMsg() throws Exception {

        // 实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-group-sync");
        // 设置NamesrvAddr的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动Producer实例
        // 此处底层使用门面模式，系统提供对外的接口，底层实际使用其它实现（defaultMQProducerImpl.start()）
        producer.start();
        // 发送同步消息失败时的重试次数
        producer.setRetryTimesWhenSendFailed(1);

        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic、Tag和消息数据
            Message msg = new Message("sync-rocketmq-topic", "tagA",
                    "sync-rocketmq".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送消息到一个Broker兵接收响应
            // 此处底层使用门面模式
            // 此处队列的选择使用Client自带的算法，不允许改变
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        // 关闭Producer实例
        producer.shutdown();
    }


    /**
     * 发送异步消息，通常使用在对响应时间敏感的业务场景
     *
     * @throws Exception MQClientException
     */
    public void asyncSendMsg() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-group-async");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        // 发送异步消息失败时的重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        // 根据消息数量实例化倒计时计算器
        final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息并指定Topic、Tag和消息数据
            Message msg = new Message("async-rocketmq-topic", "TagB",
                    "async-rocketmq".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回消息的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("当前消息序号：" + index + " | 当前MsgId：" + sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println("消息发布异常");
                    e.getStackTrace();
                }
            });
        }
        // 因为是异步，所以可能存在消息还没发送到MQ就把生产者关闭了，所以设置5S延时的等待
        countDownLatch.await(5, TimeUnit.MINUTES);
        // 关闭Producer实例
        producer.shutdown();
    }

    /**
     * 单向发送消息，通常用于不关心发送结果的场景
     *
     * @throws Exception MQClientException
     */
    public void oneWaySendMsg() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-group-oneway");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        for (int i = 0; i < 100; i++) {
            // 创建消息，执行Topic、Tag和消息数据
            Message msg = new Message("oneway-rocketmq-topic", "TagC",
                    "oneway-rocketmq".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，无返回结果
            producer.sendOneway(msg);
        }
        producer.shutdown();
    }

    /**
     * 发布顺序消息（发送到同一队列）
     *
     * @throws Exception MQClientException
     */
    public void orderSendMsg() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("order-send-group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        String[] tags = new String[]{"TagA", "TagB", "TagC"};
        // 订单列表
        List<OrderStep> orderList = new Producer().buildOrders();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        for (int i = 0; i < 10; i++) {
            // 时间前缀
            String body = dateStr + " Order Message " + orderList.get(i);
            Message msg = new Message("order-topic", tags[i % tags.length], "KEY" + i, body.getBytes());

            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {

                /**
                 * Queue队列选择
                 *
                 * @param mqs 当前Topic中包含的所有队列
                 * @param msg 发布的消息
                 * @param arg 这里使用的是send()方法的第三个参数args，此次使用的是，实际业务中可以通过该参数的Hash来选择队列
                 * @return Message
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    // 根据订单Id选择发送Queue
                    Long id = (Long) arg;
                    long index = id % mqs.size();
                    return mqs.get((int)index);
                }
            }, orderList.get(i).getOrderId(), 2000);

            System.out.printf("SendResult status:%s, queueId:%d, body:%s%n",
                    sendResult.getSendStatus(),
                    sendResult.getMessageQueue().getQueueId(),
                    body);
        }
        // 关闭Producer实例
        producer.shutdown();
    }

    /**
     * 发布延时消息
     *
     * @throws Exception MQClientException
     */
    public void delayProducer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("delay-group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        int totalMessagesToSend = 100;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("delay-topic", ("Hello scheduled message " + i).getBytes());
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            // RocketMQ当前支持18个等级的延时设置，分别对应：
            // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            message.setDelayTimeLevel(3);
            // 发送消息
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();
    }

    /**
     * 事务消息
     *
     * @throws Exception MQClientException
     */
    public void transactionSendMsg() throws Exception {

        TransactionMQProducer producer = new TransactionMQProducer("my-transaction-producer");
        producer.setNamesrvAddr("localhost:9876");

        // 消息发布成功之后的回调，执行业务逻辑
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                LocalTransactionState state = null;
                // msg-4返回COMMIT_MESSAGE
                if(message.getKeys().equals("msg-1")){
                    state = LocalTransactionState.COMMIT_MESSAGE;
                }
                // msg-5返回ROLLBACK_MESSAGE
                else if(message.getKeys().equals("msg-2")){
                    state = LocalTransactionState.ROLLBACK_MESSAGE;
                }else{
                    // 这里返回unknown的目的是模拟执行本地事务突然宕机的情况（或者本地执行成功发送确认消息失败的场景）
                    state = LocalTransactionState.UNKNOW;
                }
                System.out.println(message.getKeys() + ",state:" + state);
                return state;
            }

            /**
             * 事务消息的回查方法，如果Broker未收到确认结果，会主动回查，需查询业务逻辑事务的执行结果再给Broker响应
             * 回查的时间间隔和次数都是可配的，默认是回查15次还失败的话就会把这个消息丢掉了
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                if (null != messageExt.getKeys()) {
                    switch (messageExt.getKeys()) {
                        case "msg-3":
                            System.out.println("msg-3 unknow");
                            return LocalTransactionState.UNKNOW;
                        case "msg-4":
                            System.out.println("msg-4 COMMIT_MESSAGE");
                            return LocalTransactionState.COMMIT_MESSAGE;
                        case "msg-5":
                            // 查询到本地事务执行失败，需要回滚消息
                            System.out.println("msg-5 ROLLBACK_MESSAGE");
                            return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        producer.start();

        // 模拟发送5条消息
        for (int i = 1; i < 6; i++) {
            try {
                Message msg = new Message("transactionTopic", null, "msg-" + i, ("测试，这是事务消息！ " + i).getBytes());
                producer.sendMessageInTransaction(msg, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void defineQueueSelect() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("define-queue-select");
        producer.setNamesrvAddr("localhost:9876");
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message = new Message("define-select",
                    ("message" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 直接使用
            producer.send(message, new SelectMessageQueueByHash(), i);
        }
    }

    /**
     * 订单的步骤
     */
    private static class OrderStep {
        private long orderId;
        private String desc;

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "OrderStep{" +
                    "orderId=" + orderId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    /**
     * 生成模拟订单数据
     */
    private List<OrderStep> buildOrders() {
        List<OrderStep> orderList = new ArrayList<OrderStep>();

        OrderStep orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }
}
