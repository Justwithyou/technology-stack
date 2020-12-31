# Kafka

Kafka官网：http://kafka.apache.org/
Kafka下载：http://kafka.apache.org/downloads
Kafka快速入门：http://kafka.apache.org/quickstart

## Kafka基础

Kafka：是一款分布式流处理平台，高性能、高吞吐量的消息发布和订阅系统（分布式的、支持分区、多副本的、基于Zookeeper的分布式消息系统）

Kafka由Scala语言编写，Apache顶级开源项目

三大核心功能：

1. 发布和订阅数据流
2. 存储流式数据，并具有较好的容错性
3. 可以实时处理流数据

适用场景：

1. 构造实时流数据管道，在系统或者应用之间可靠的获取数据
2. 构建实时流应用程序，并对流数据进行转换或处理

概念：

1. Kafka作为集群，运行在一台或者多台服务器上
2. Kafka通过Topic对存储的流数据进行分类
3. 每条记录中包含一个key、一个value和一个时间戳

四组核心API接口：

1. The Producer API 允许一个应用程序发布一串流式的数据到一个或多个Kafka Topic
2. The Consumer API 允许一个应用程序订阅一个或多个Kafka Topic并对发布给他们的流式数据进行处理
3. The Stream API 允许一个应用程序作为一个流处理器，消费一个或者多个Kafka Topic产生的输入流，然后生产一个输出流
到一个或多个Kafka Topic中去，在输入输出流中进行有效的转换
4. The Connector API 允许构建并运行可重用的生产者和消费者，将Kafka Topic连接到已存在的应用程序或者数据系统中

Kafka中，客户端和服务端使用简单的、高性能的、支持多语言的TCP协议

Kafka的常见使用场景：

1. 日志收集
2. 消息系统
3. 用户活动追踪
4. 运营指标
5. 流式处理
6. 事件源

Kafka的设计思想：

1. Kafka的Broker-Leader选举，所有的Kafka-Broker节点都会去Zookeeper上注册临时节点，只有一个会注册成功成为Kafka-Broker-Controller节点，
其他的都是Kafka-Follower节点，如果该节点宕机，则所有Follower节点都会去Zookeeper上重新注册临时节点（有点疑问：这里的Leader就是指的Partition领导者选举？），
如果有一台Broker节点宕机，则会读取宕机的Broker机器上所有的Partition在Zookeeper上的状态，如有Partition为Leader则选取该Partition的ISR副本中的
一个Replica作为新的Leader领导者，若该Partition的所有ISR副本全挂，则选择一个幸存的Replica作为领导者，若所有副本全挂，则把新的Leader设置为-1直到
ISR中的任一Replica恢复或者选择第一个恢复的Replica副本（这里是两种策略选择）

2. 消费组中消费者和Topic中分区的数量保持一致性（推荐）

3. 消息传输一致性也成消费的三种模式（最少一次、最多一次、恰好一次）

> At-most-once：最多一次
>
> 1. 设置enable-auto-commit为true
> 2. 设置auto-commit-interval.ms为一个较小的时间间隔
> 3. 客户端不需要调用commitSync()方法，会在特定的时间间隔自动提交
>
> At-least-once：最少一次
>
> 方法一
> 1. 设置enable-auto-commit为false
> 2. 客户端调用commitSync()，增加消息偏移量
>
> 方法二
> 1. 设置enable-auto-commit为true
> 2. 设置auto-commit-interval-ms为一个较大的时间间隔
> 3. 客户端调用commitSync()方法，增加消息偏移量
>
> Exactly-once：正好一次，实现这种方式，需要自己控制消息的offset偏移量，对消息的处理和offset的移动必须保持在同一个事务中
>
> 1. 设置enable-auto-commit为false
> 2. 保存offset到数据库
> 3. Partition分区变化的时候需要Rebalance重新分配，有如下事件会触发分区变化：
>   3.1 consumer订阅的topic中的分区大小发生变化
>   3.2 topic被创建或者被删除
>   3.3 consumer所在的消费组group有消费者挂掉
>   3.4 新的consumer加入了消费组中
> 此时consumer通过实现ConsumerRebalanceListener接口，捕捉这些事件，手动对偏移量进行处理
> 4. consumer通过调用seek(TopicPartition, long)方法，移动到指定分区的偏移量

### 基础概念

* Topic: 主题，代表消息类别
* Producer: 生产者，发布指定Topic消息到服务端的进程，通过TCP协议与Kafka服务端通信
* Consumer: 消费者，从服务端获取指定Topic消息的进程，通过TCP协议与Kafka服务端通信
* Broker: Kafka集群中的每一台服务器都叫Broker

#### Topic

Topic代表着数据的类别，数据发布的地方，可用于区分业务系统，Kafka中的Topic是多订阅者模式，一个Topic可以拥有一个或者多个消费者订阅它的数据

每个Topic会被划分为多个Partition分区，每个分区都是有序且顺序不可变的记录集，并且不断地追加到结构化的append log文件末尾，分区中的每一条记录都会
分配一个id号表示顺序，称为offset偏移量，offset用来唯一标识分区中的每一条记录，Kafka未提供额外的机制保存offset偏移量而是由消费者控制，消费者可以
采用任何顺序来消费记录（消费在分区中的位置）

通常情况下应该禁用自动创建Topic属性（）

Kafka集群会保存发布的记录，不论是否已经被消费过，并通过一个可配置的参数控制保留的期限，期限过后则会丢弃数据并释放磁盘空间，Kafka性能和数据大小无关

Partition分区的用途：

1. 可以方便的扩展，每个Partition都是受限于主机的文件限制，而Topic可以允许多个分区
2. 并行读写

Partition：在系统中表现为一个个的文件夹，内部包含多个segment文件，每个文件中包含数据文件log和索引文件index

#### Producer

Producer可以把消息发送到Kafka集群的指定Topic中，同时也可以指定Partition分区并将消息持久化到特定的Partition中

如果没有指定Partition则Kafka可以通过一定的算法计算出对应的Partition分区：

1. 如果发送的消息指定了Key则对Key进行Hash然后映射到对应的Partition分区
2. 如果发送的消息没有指定Key则使用Round Robin轮询算法确认Partition分区，这样可以保证数据在所有的Partition分区中平均分配   

此外，Kafka的Producer也支持自定义的Partition分配方式，客户端提供org.apache.kafka.clients.producer.Partitioner类配置到Producer即可

Producer发送消息给Kafka集群时，通常由Partition分区中的leader处理所有读写请求，Kafka允许Producer使用不同的策略确认消息是否发送成功（ACK）

1. 只要发送到Kafka集群即认为成功，效率最高，结果不保证
2. 只要收到leader响应的成功即认为成功
3. 等待所有follower同步成功之后leader响应成功才认为成功，效率低，但结果有保证

#### Consumer

Consumer通过改变offset的值来顺序读取消息，在Kafka0.9版本之前，offset信息保存在Zookeeper的/consumers/{group}/offsets/{topic}/{partition}
目录中，在Kafka0.9版本之后，所有的offset信息保存在Broker上的一个_consumer_offsets的Topic中

传统的消息队列的两种消费模式：

1. 队列模式（点对点模式）：一条消息只能被多个消费者中的一个消费
2. 发布订阅模式：一条消息能被多个消费者同时消费

Kafka中的消费者：多个消费者可以被放在一个消费者组中，一个消费者必须属于一个消费者组，同时一个消费者组能够拥有多个消费者

Kafka消费者组的约束：

1. 一条消息（一个Partition分区）只能被一个消费者组中的一个消费者消费
2. 同一个Partition中的消息只能被某个消费者组中的某个固定消费者消费

#### Replication

一个Topic中的多个Partition分区，被分布在Kafka集群中的多个Broker上，Kafka支持为每个Partition分区设置副本（Replicas）的个数，所有的
备份Partition分布在Kafka集群中，提高可用性，确保容错性

每个Partition分区的副本中，都有一台机器被称为leader领导者，其他零台或多台机器称为follower跟随者，leader负责所有的读写操作，follower被动的同步
leader中的数据，如果leader所在机器宕机，则follower中的一台机器会自动称为新的leader领导者，每台机器都会成为某些分区的leader和某些分区的
follower，因此集群负载是均衡的，实质上所有的读写请求都是都是针对leader的操作，leader操作完成之后，发送指令给follower进行数据同步

Replicas副本中有一些副本可以保持和leader准实时同步，称为ISR副本，当leader挂掉之后，只会从ISR副本中选择一台机器称为新的leader领导者，其他的普通
follower副本不会被选择

### Kafka安装和启动

1. 下载Kafka安装包

下载时可选择需要的Scala版本配套的Kafka版本安装包

```shell script
tar -zxvf kafka_2.13-2.7.0.tgz
cd kafka_2.12-2.7.0
```

2. 启动服务

运行Kafka需要Zookeeper服务，如果没有Zookeeper的话，可以使用Kafka自带打包和配置好的内置Zookeeper服务

```shell script
bin/zookeeper-server-start.sh config/zookeeper.properties &

bin/kafka-server-start.sh config/server.properties &
```

3. 代码查看spring-kafka服务

### Kafka的关键配置

> buffer.memory：内存缓冲区的大小，KafkaProducer发送消息时都是先进入到客户端的内存缓冲区中，然后把消息收集成一个个的Batch再发送到Broker上，
> 默认大小是32MB，如果该值设置的太小，可能出现消息被快速写入缓冲区，sender线程来不及发送给Broker时内存缓冲区就被写满导致用户线程阻塞
> 
> batch.size：每个Batch要存放的数据大小后，才会发送消息，默认大小是16KB，数值升高，可以允许更多的数据在缓冲区，一次发送
> 的数据量变大，提高吞吐量，如果设置过大也可以能导致消息发送延迟过高
>
> linger.ms：一个Batch被创建之后，最多过多久，不管Batch有没有写满都会被发送，配置batch.size一起设置，可以避免消息积压在内存中
>
> max.request.size：每次发送给Kafka服务器的请求消息的最大大小
>
> retries：重试机制，可以重试次数
>
> retries.backoff.ms：每次重试的时间间隔
>
> acks：发布消息时的响应策略
> 0：KafkaProducer往集群发送消息时，不需要等待集群的返回，安全性较低效率较高
> 1：KafkaProducer往集群发送消息时，只要leader响应成功即可，中和安全性和效率
> all：KafkaProducer往集群发送消息时，需要所有的ISR/Follower都完成从leader的同步才认为成功，安全性高效率较低

## Kafka常见问题

1. 重复消费

两个消费者参数：max.poll.interval.ms（两个poll操作的最大时间间隔）、max.poll.records（一次poll操作获取的消息数量）

偏移量自动提交配置：enable-auto-commit

偏移量无效时的消费位置配置：auto-offset-reset

## Kafka Connect

Kafka Connect是一种用于在Kafka和其他系统之间可扩展的、可靠的流式传输数据的工具，使得定义将大量数据集合输入和输出Kafka的
连接器变得简单

特点：

1. Kafka Connector通用框架，提供统一的集成API
2. 同时支持分布式模式和单机模式
3. REST接口，用来查看和管理Kafka Connectors
4. 自动化的offset管理
5. 分布式、可扩展
6. 流/批处理集成

Kafka Connector的两个核心：source（负责输入数据到Kafka）和sink（负责输出Kafka数据），都被称为Connector

相关概念：

> Connector：通过管理task来协调数据流的高级抽象，定义数据的来源及去向
> Task：如何将数据复制到Kafka或从Kafka复制数据的实现，
> Workers：执行Connector和Task的运行进程
> Converters：用于在Connector和外部系统发送或接收数据之间转换数据的代码
> Transforms：更改由连接器生成或发送到连接器的每个消息的简单逻辑













