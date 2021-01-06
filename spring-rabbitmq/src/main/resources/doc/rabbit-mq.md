# RabbitMQ系列

RabbitMQ官网：https://www.rabbitmq.com/
Erlang官网：https://www.erlang.org/
GitHub源码：https://github.com/rabbitmq/rabbitmq-tutorials

## RabbitMQ基础

RabbitMQ：开源的消息代理的队列服务器，使用Erlang语言编写，基于AMQP协议

**本章节代码请查看spring-rabbitmq模块**

特点：

1. 开源、性能优秀，稳定性保障
2. 提供可靠性消息投递模式
3. 与Spring AMQP完美整合，API丰富
4. 集群模式丰富，表达式配置，HA模式，镜像队列模型
5. 保障数据不丢失的前提做到高可用

典型应用场景：

1. 异步处理
2. 流量削峰
3. 日志处理
4. 应用解耦

AMQP协议：提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计（Advanced Message Queuing Protocol）

常见协议类型：AMQP/STOMP/MQTT/XMPP/-基于TCP/IP的协议簇

重要概念：
> Server：接收客户端的连接，实现AMQP实体服务
> Connection：连接，应用程序和Server的网络连接，TCP连接
> Channel：信道，消息读写等操作在信道中进行，客户端建立多个信道，每个信道代表一个会话任务
> Message：消息，应用程序和服务器之间传递的数据，有Properties和Body组成，Properties为外包装，修饰消息（如优先级、延迟），Body为消息体内容
> Virtual Host：虚拟主机，用于逻辑隔离，一个虚拟主机里面可以有多个Exchange和Queue队列，但名称必须唯一
> Exchange：交换器，接收消息，按照路由规则将消息路由到一个或多个队列，如果路由不到，则直接返回消费者或者直接丢弃，常用的交换器类型有direct/topic/fanout/headers四种
> Binding：绑定，交换器和消息队列之间的虚拟连接，绑定中可以包含一个或者多个RoutingKey
> RoutingKey：路由键，生产者将消息发送给交换器的时候，同时会发送一个RoutingKey，用来指定路由规则
> Queue：消息队列，用来保存消息，供消费者消费

AMQP协议由三部分组成：生产者、消费者、服务端

常用交换器类型：direct、topic、fanout、headers四种

Direct Exchange：该类型的交换器将所有发送到该交换器的消息转发到RoutingKey指定的队列中，也就是路由到BindingKey和RoutingKey完全匹配的队列
Topic Exchange：该类型的交换器将所有发送到Topic Exchange的消息转发到RoutingKey中指定的Topic的队列上面，Exchange将RoutingKey和Topic进行模糊匹配
Fanout Exchange：该类型的交换器不处理路由键，会把所有发送到交换器的消息路由到所有绑定的队列中，优点是转发消息最快，性能最佳
Headers Exchange：该类型的交换器不依赖路由规则来路由消息，而是根据消息内容中的headers属性进行匹配，性能较差

RabbitMQ中的RPC：Remote Procedure Call，远程过程调用，同步执行

RabbitMQ的六种工作模式：

1. simple简单模式
2. work工作模式
3. publish/subscribe发布订阅模式
4. routing路由模式
5. topic主题模式
6. rpc模式

RabbitMQ中队列、交换器、消息的持久化都可以在生产者发布消息时设置参数

## RabbitMQ集群

RabbitMQ使用详解：[https://www.cnblogs.com/ithushuai/p/12443460.html]

### 单一模式

非集群

### 普通模式

默认的集群模式，对于Queue来说，消息实体只存在于其中一个节点，所有节点中只有相同的元数据，当从其他节点拉取数据时，消息会在节点
之间传输，所以Consumer应该尽量连接每一个节点

### 镜像模式

消息实体会在镜像节点之间主动同步

## RabbitMQ相关名词

### Message Acknowledgment

消息确认

在实际应用中，可能会发生消费者收到Queue中的消息，但没有处理完成就宕机（或出现其他意外）的情况，这种情况下就可能会导致消息丢失；
为了避免这种情况发生，我们可以要求消费者在消费完消息后发送一个回执给RabbitMQ，RabbitMQ收到消息回执（Message acknowledgment）后才将该
消息从Queue中移除；如果RabbitMQ没有收到回执并检测到消费者的RabbitMQ连接断开，则RabbitMQ会将该消息发送给其他消费者（如果存在多个消费者）
进行处理；这里不存在Timeout概念，一个消费者处理消息时间再长也不会导致该消息被发送给其他消费者，除非它的RabbitMQ连接断开

#### Message Durability

消息持久化

如果我们希望即使在RabbitMQ服务重启的情况下，也不会丢失消息，我们可以将Queue与Message都设置为可持久化的（durable），这样可以保证
绝大部分情况下我们的RabbitMQ消息不会丢失；但依然解决不了小概率丢失事件的发生（比如RabbitMQ服务器已经接收到生产者的消息，
但还没来得及持久化该消息时RabbitMQ服务器就断电了），如果我们需要对这种小概率事件也要管理起来，那么我们要用到事务

#### Prefetch Count

如果有多个消费者同时订阅同一个Queue中的消息，Queue中的消息会被平摊给多个消费者；这时如果每个消息的处理时间不同，
就有可能会导致某些消费者一直在忙，而另外一些消费者很快就处理完手头工作并一直空闲的情况；我们可以通过设置prefetchCount来限制Queue每次发送给
每个消费者的消息数，比如我们设置prefetchCount=1，则Queue每次给每个消费者发送一条消息；消费者处理完这条消息后Queue会再给该消费者发送一条消息

## 实际开发

RabbitConfig：定义多个Exchange|Queue|RoutingKey，同时通过路由关键字绑定交换机和队列



