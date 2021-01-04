# RabbitMQ系列

RabbitMQ：开源的消息代理的队列服务器，使用Erlang语言编写，基于AMQP协议

RabbitMQ官网：https://www.rabbitmq.com/
Erlang官网：https://www.erlang.org/
GitHub源码：https://github.com/rabbitmq/rabbitmq-tutorials

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

