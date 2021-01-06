# RocketMQ

RocketMQ官网：[http://rocketmq.apache.org/]

GitHub地址：[https://github.com/apache/rocketmq]

Apache下载镜像地址：[https://mirror.bit.edu.cn/apache/]

RocketMQ官网下载：[http://rocketmq.apache.org/dowloading/releases/]

## 1. 概念和特性 

### 概念（Concept）

#### 1. 消息模型（Message Model）

RocketMQ主要由Producer、Broker、Consumer 三部分组成，其中Producer 负责生产消息，Consumer 负责消费消息，Broker 负责存储消息；
Broker 在实际部署过程中对应一台服务器，每个Broker 可以存储多个Topic的消息，每个Topic的消息也可以分片存储于不同的 Broker；
Message Queue 用于存储消息的物理地址，每个Topic 中的消息地址存储于多个 Message Queue 中；
ConsumerGroup 由多个Consumer 实例构成；

#### 2. 消息生产者（Producer）

负责生产消息，一般由业务系统负责生产消息，一个消息生产者会把业务应用系统里产生的消息发送到Broker 服务器；
RocketMQ提供多种发送方式，同步发送、异步发送、顺序发送、单向发送；同步和异步方式均需要Broker 返回确认信息，单向发送不需要
常用的Producer类为DefaultMQProducer类

#### 3. 消息消费者（Consumer）

负责消费消息，一般是后台系统负责异步消费，一个消息消费者会从Broker 服务器拉取消息、并将其提供给应用程序；
从用户应用的角度而言提供了两种消费形式：拉取式消费、推动式消费
常用的Consumer类有：DefaultMQPushConsumer类（收到消息之后自动调用传入的处理方法处理，实时性较高），DefaultMQPullConsumer类（用户
自主控制何时调用，灵活性较高）

Consumer每隔30S从NameServer获取Topic的最新队列情况，这意味着Broker不可用时，Consumer最多需要30S才能感知

Consumer每隔30S从（可由ClientConfig中的heartbeatBrokerInterval属性决定）向所有关联的Broker发送心跳，Broker每隔10S扫描所有存活的连接，
若某个连接2分钟内没有发送心跳数据，则关闭连接，并向该Consumer所在的ConsumerGroup发出通知，Group内的Consumer重新分配队列，再继续消费

#### 4. 主题（Topic）

表示一类消息的集合，每个主题包含若干条消息，每条消息只能属于一个主题，是RocketMQ 进行消息订阅的基本单位，默认一个Topic包含四个Queue

消息的逻辑分类，发消息之前必须指定Topic才能发布，消费时页需要指定Topic进行消费

#### 5. 代理服务器（Broker Server）

消息中转角色，负责存储消息、转发消息。代理服务器在RocketMQ系统中负责接收从生产者发送来的消息并存储、同时为消费者的拉取请求作准备；
代理服务器也存储消息相关的元数据，包括消费者组、消费进度偏移和主题和队列消息等

Broker分为Master和Slave，一个Master可以对应多个Slave，但是一个Slave只能对应一个Master，Master和Slave的对应关系可以通过指定
相同的Broker Name，不同的Broker Id来定义，Broker Id为0表示Master，非0表示Slave，Master可以部署多个

单Master模式、一个Master对应一个或多个Slave模式、多个Master对应多个Slave的同步双写模式、多个Master对应多个Slave的异步复制模式

#### 6. 名称服务（Name Server）

名称服务充当路由消息的提供者。生产者或消费者能够通过名字服务查找各主题相应的Broker IP列表；
多个NameServer实例组成集群，但相互独立，没有信息交换

消息队列中的状态服务器，集群的各个组件通过它来获取全局的信息

热备份：NameServer可以部署多个，相互之间独立，无信息交换，其它角色都同时向所有NameServer上报状态信息

心跳机制：NameServer中的Broker、Topic等状态信息不会持久化存储，都是由各个角色定时（默认30S）上报并存储到内存中，超时（2小时）不上报的话，
NameServer会认为某台机器出故障不可用

#### 7. 拉取式消费（Pull Consumer）

Consumer消费的一种类型，应用通常主动调用Consumer的拉消息方法从Broker服务器拉消息、主动权由应用控制；
一旦获取了批量消息，应用就会启动消费过程

#### 8. 推动式消费（Push Consumer）

Consumer消费的一种类型，该模式下Broker收到数据后会主动推送给消费端，该消费模式一般实时性较高

#### 9. 生产者组（Producer Group）

同一类Producer的集合，这类Producer发送同一类消息且发送逻辑一致；
如果发送的是事务消息且原始生产者在发送之后崩溃，则Broker服务器会联系同一生产者组的其他生产者实例以提交或回溯消

#### 10. 消费者组（Consumer Group）

同一类Consumer的集合，这类Consumer通常消费同一类消息且消费逻辑一致。消费者组使得在消息消费方面，实现负载均衡和容错的目标变得非常容易；
要注意的是，消费者组的消费者实例必须订阅完全相同的Topic，RocketMQ支持两种消息模式：集群消费（Clustering）和广播消费（Broadcasting）

#### 11. 集群消费（Clustering）

集群消费模式下，相同Consumer Group的每个Consumer实例平均分摊消息

#### 12. 广播消费（Broadcasting）

广播消费模式下，相同Consumer Group的每个Consumer实例都接收全部的消息

#### 13. 普通顺序消息（Normal Ordered Message）

普通顺序消费模式下，消费者通过同一个消费队列收到的消息是有顺序的，不同消息队列收到的消息则可能是无顺序的

#### 14. 严格顺序消息（Strictly Ordered Message）

严格顺序消息模式下，消费者收到的所有消息均是有顺序的

#### 15. 消息（Message）

消息系统所传输信息的物理载体，生产和消费数据的最小单位，每条消息必须属于一个主题；
RocketMQ中每个消息拥有唯一的Message ID，且可以携带具有业务标识的Key，系统提供了通过Message ID和Key查询消息的功能

#### 16. 标签（Tag）

为消息设置的标志，用于同一主题下区分不同类型的消息；
来自同一业务单元的消息，可以根据不同业务目的在同一主题下设置不同标签；
标签能够有效地保持代码的清晰度和连贯性，并优化RocketMQ提供的查询系统；
消费者可以根据Tag实现对不同子主题的不同消费逻辑，实现更好的扩展性

#### 17. 队列（Queue）

一个Topic会被分为N个Queue，数量是可配置的，Message实际是存储到Queue上的，消费者消费的也是Queue上的消息，默认一个Topic分为四个Queue

RocketMQ是通过主从模式实现消息的冗余（即类似Kafka中的副本）

#### 18. ACK机制

ACK机制是发生在Consumer端的，不是在Producer端的；也就是说Consumer消费完消息后要进行ACK确认，如果未确认则代表是消费失败，
这时候Broker会进行重试策略（仅集群模式会重试）

### 特性（Features）

#### 1. 订阅与发布

消息的发布是指某个生产者向某个topic发送消息；
消息的订阅是指某个消费者关注了某个topic中带有某些tag的消息，进而从该topic消费数据

#### 2. 消息顺序

消息有序指的是一类消息消费时，能按照发送的顺序来消费；
例如：一个订单产生了三条消息分别是订单创建、订单付款、订单完成。消费时要按照这个顺序消费才能有意义，但是同时订单之间是可以并行消费的；
RocketMQ可以严格的保证消息有序

顺序消息分为全局顺序消息与分区顺序消息，全局顺序是指某个Topic下的所有消息都要保证顺序；部分顺序消息只要保证每一组消息被顺序消费即可

* 全局顺序：对于指定的一个 Topic，所有消息按照严格的先入先出（FIFO）的顺序进行发布和消费。 适用场景：性能要求不高，
 所有的消息严格按照 FIFO 原则进行消息发布和消费的场景

* 分区顺序：对于指定的一个 Topic，所有消息根据 sharding key 进行区块分区。 同一个分区内的消息按照严格的 FIFO 顺序进行发布和消费；
 Sharding key 是顺序消息中用来区分不同分区的关键字段，和普通消息的 Key 是完全不同的概念；
 适用场景：性能要求高，以 sharding key 作为分区字段，在同一个区块中严格的按照 FIFO 原则进行消息发布和消费的场景

#### 3. 消息过滤

RocketMQ的消费者可以根据Tag进行消息过滤，也支持自定义属性过滤；消息过滤目前是在Broker端实现的，优点是减少了对于Consumer无用消息的网络传输，
缺点是增加了Broker的负担、而且实现相对复杂

#### 4. 消息可靠性

RocketMQ支持消息的高可靠，影响消息可靠性的几种情况：

1. Broker非正常关闭
2. Broker异常Crash
3. OS Crash
4. 机器掉电，但是能立即恢复供电情况
5. 机器无法开机（可能是cpu、主板、内存等关键设备损坏）
6. 磁盘设备损坏
1)、2)、3)、4) 四种情况都属于硬件资源可立即恢复情况，RocketMQ在这四种情况下能保证消息不丢，或者丢失少量数据（依赖刷盘方式是同步还是异步）。

5)、6)属于单点故障，且无法恢复，一旦发生，在此单点上的消息全部丢失。RocketMQ在这两种情况下，通过异步复制，可保证99%的消息不丢，
但是仍然会有极少量的消息可能丢失。通过同步双写技术可以完全避免单点，同步双写势必会影响性能，适合对消息可靠性要求极高的场合，
例如与Money相关的应用；注：RocketMQ从3.0版本开始支持同步双写

#### 5. 至少一次

至少一次(At least Once)指每个消息必须投递一次，Consumer先Pull消息到本地，消费完成后，才向服务器返回ack，如果没有消费一定不会ack消息，
所以RocketMQ可以很好的支持此特性

#### 6. 回溯消费

回溯消费是指Consumer已经消费成功的消息，由于业务上需求需要重新消费，要支持此功能，Broker在向Consumer投递成功消息后，消息仍然需要保留；
并且重新消费一般是按照时间维度，例如由于Consumer系统故障，恢复后需要重新消费1小时前的数据，那么Broker要提供一种机制，
可以按照时间维度来回退消费进度；RocketMQ支持按照时间回溯消费，时间维度精确到毫秒

#### 7. 事务消息

RocketMQ事务消息（Transactional Message）是指应用本地事务和发送消息操作可以被定义到全局事务中，要么同时成功，要么同时失败；
RocketMQ的事务消息提供类似 X/Open XA 的分布事务功能，通过事务消息能达到分布式事务的最终一致

#### 8. 定时消息（延时消息）

定时消息（延迟队列）是指消息发送到broker后，不会立即被消费，等待特定时间投递给真正的topic； broker有配置项messageDelayLevel，
默认值为“1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h”，18个level；可以配置自定义messageDelayLevel；
注意，messageDelayLevel是broker的属性，不属于某个topic；

发消息时，设置delayLevel等级即可：msg.setDelayLevel(level)。level有以下三种情况：

* level == 0，消息为非延迟消息
* 1<=level<=maxLevel，消息延迟特定时间，例如level==1，延迟1s
* level > maxLevel，则level== maxLevel，例如level==20，延迟2h
定时消息会暂存在名为SCHEDULE_TOPIC_XXXX的topic中，并根据delayTimeLevel存入特定的queue，queueId = delayTimeLevel – 1，
即一个queue只存相同延迟的消息，保证具有相同发送延迟的消息能够顺序消费。broker会调度地消费SCHEDULE_TOPIC_XXXX，将消息写入真实的topic

需要注意的是，定时消息会在第一次写入和调度写入真实topic时都会计数，因此发送数量、tps都会变高

#### 9. 消息重试

Consumer消费消息失败后，要提供一种重试机制，令消息再消费一次。Consumer消费消息失败通常可以认为有以下几种情况：

* 由于消息本身的原因，例如反序列化失败，消息数据本身无法处理（例如话费充值，当前消息的手机号被注销，无法充值）等；这种错误通常需要跳过这条消息，
再消费其它消息，而这条失败的消息即使立刻重试消费，99%也不成功，所以最好提供一种定时重试机制，即过10秒后再重试

* 由于依赖的下游应用服务不可用，例如db连接不可用，外系统网络不可达等。遇到这种错误，即使跳过当前失败的消息，消费其他消息同样也会报错；
这种情况建议应用sleep 30s，再消费下一条消息，这样可以减轻Broker重试消息的压力

RocketMQ会为每个消费组都设置一个Topic名称为“%RETRY%+consumerGroup”的重试队列（这里需要注意的是，这个Topic的重试队列是针对消费组，
而不是针对每个Topic设置的），用于暂时保存因为各种异常而导致Consumer端无法消费的消息；考虑到异常恢复起来需要一些时间，
会为重试队列设置多个重试级别，每个重试级别都有与之对应的重新投递延时，重试次数越多投递延时就越大；
RocketMQ对于重试消息的处理是先保存至Topic名称为SCHEDULE_TOPIC_XXXX的延迟队列中，
后台定时任务按照对应的时间进行Delay后重新保存至“%RETRY%+consumerGroup”的重试队列中

#### 10. 消息重投

生产者在发送消息时，同步消息失败会重投，异步消息有重试，oneway没有任何保证。消息重投保证消息尽可能发送成功、不丢失，但可能会造成消息重复，消息重复在RocketMQ中是无法避免的问题。消息重复在一般情况下不会发生，当出现消息量大、网络抖动，消息重复就会是大概率事件。另外，生产者主动重发、consumer负载变化也会导致重复消息。如下方法可以设置消息重试策略：

* retryTimesWhenSendFailed:同步发送失败重投次数，默认为2，因此生产者会最多尝试发送retryTimesWhenSendFailed + 1次；
不会选择上次失败的broker，尝试向其他broker发送，最大程度保证消息不丢。超过重投次数，抛出异常，由客户端保证消息不丢；
当出现RemotingException、MQClientException和部分MQBrokerException时会重投

* retryTimesWhenSendAsyncFailed:异步发送失败重试次数，异步重试不会选择其他broker，仅在同一个broker上做重试，不保证消息不丢

* retryAnotherBrokerWhenNotStoreOK:消息刷盘（主或备）超时或slave不可用（返回状态非SEND_OK），是否尝试发送到其他broker，默认false

#### 11. 流量控制

生产者流量控制，因为Broker处理能力达到瓶颈；消费者流量控制，因为消费能力达到瓶颈

生产者流量控制：

* commitLog文件被锁时间超过osPageCacheBusyTimeOutMills时，参数默认为1000ms，返回流控。
* 如果开启transientStorePoolEnable == true，且broker为异步刷盘的主机，且transientStorePool中资源不足，拒绝当前send请求，返回流控。
* broker每隔10ms检查send请求队列头部请求的等待时间，如果超过waitTimeMillsInSendQueue，默认200ms，拒绝当前send请求，返回流控。
* broker通过拒绝send 请求方式实现流量控制

注意：生产者流量控制，不会尝试消息重投

消费者流量控制：

* 消费者本地缓存消息数超过pullThresholdForQueue时，默认1000
* 消费者本地缓存消息大小超过pullThresholdSizeForQueue时，默认100MB
* 消费者本地缓存消息跨度超过consumeConcurrentlyMaxSpan时，默认2000

消费者流量控制的结果是拉低拉取的频率

#### 12. 死信队列

死信队列用于处理无法被正常消费的消息；当一条消息初次消费失败，消息队列会自动进行消息重试；达到最大重试次数后，若消费依然失败，
则表明消费者在正常情况下无法正确地消费该消息，此时，消息队列 不会立刻将消息丢弃，而是将其发送到该消费者对应的特殊队列中

RocketMQ将这种正常情况下无法被消费的消息称为死信消息（Dead-Letter Message），将存储死信消息的特殊队列称为死信队列（Dead-Letter Queue）；
在RocketMQ中，可以通过使用console控制台对死信队列中的消息进行重发来使得消费者实例再次进行消费

#### 13. 通信机制

（1） Broker启动后需要完成一次将自己注册至NameServer的操作；随后每隔30s时间定时向NameServer更新Topic路由信息
（2） Producer发送消息时候，需要根据消息的Topic从本地缓存获取路由信息；如果没有则更新路由信息会从NameServer重新拉取，
同时Producer会默认每隔30s向NameServer拉取一次路由信息
（3） Consumer消费消息时候，从NameServer获取的路由信息，并再完成客户端的负载均衡后，监听指定消息队列获取消息并进行消费

## 2. 架构设计

### 架构（Architecture）

#### 角色

##### Broker

主要用于Producer和Consumer来接收和发送消息
Broker会定时（30S）向NameServer提交自己的信息
Broker是消息中间件的消息存储、转发服务器
每个Broker节点，在启动时，都会遍历NameServer列表，与每个NameServer建立长连接，注册自己的信息，之后定时上报

##### NameServer

类似Kafka中Zookeeper的效果
底层由Netty实现，提供路由管理、服务注册、服务发现的功能，是一个无状态节点
NameServer是一个服务发现者，集群中各个角色（Producer、Consumer、Broker）都需要定时向NameServer上报自己的状态，如果超时不上报的话，
NameServer会把它从列表中移除
NameServer可以部署多个，当多个NameServer存在的时候，其他角色同时向他们上报信息，以保证高可用
NameServer之间互不通信，没有主备的概念
NameServer是内存是存储，其中的Broker、Topic等信息默认不会持久化，所以是无状态节点

##### Producer

消息的生产者
随机选择一个NameServer节点建立长连接，获得Topic路由信息（包括Topic下的Queue队列及Queue在Broker上的分布等）
然后向提供Topic服务的Broker Master建立长连接（RocketMQ只有Master才能写消息），且定时向Master发送心跳

##### Consumer

消息的消费者
通过NameServer集群获取Topic的路由信息，连接到对应的Broker上消费消息
由于Master和Slave都可以读取消息，所以Consumer会与Master和Slave都建立连接进行消费消息

#### 核心流程

* Broker都注册到Nameserver上

* Producer发消息的时候会从Nameserver上获取发消息的topic信息

* Producer向提供服务的所有master建立长连接，且定时向master发送心跳

* Consumer通过NameServer集群获得Topic的路由信息

* Consumer会与所有的Master和所有的Slave都建立连接进行监听新消息

### 设计（Design）

## 3. 安装部署

### Linux安装RocketMQ

1. 下载压缩包

```shell script
wget https://mirror.bit.edu.cn/apache/rocketmq/4.7.0/rocketmq-all-4.7.0-bin-release.zip
```

2. 安装JDK

RocketMQ使用Java语言编写，需要安装JDK环境
···

3. 解压安装RocketMQ

```shell script
unzip rocketmq-all-4.7.0-bin-release.zip
```

4. 启动

4.1 启动NameServer

```shell script
cd rocketmq-all-4.7.0-bin-release/bin
nohup ./mqnamesrv &
```

4.2 启动Broker

启动Broker时需要指定NameServer的地址，如有多个可以;分隔
```shell script
cd rocketmq-all-4.7.0-bin-release/bin
nohup ./mqbroker -n localhost:9876 &
```

常见错误：启动Broker失败'Cannot allocate memory'

解决方案：

由于默认分配内存太大，超出机器内存，直接OOM

修改bin目录下的两个脚本：runbroker.sh/runserver.sh，在两个脚本中搜索-server -Xms将其内存分配减少  

### RocketMQ控制台的安装

安装方法通常有两种：

* 第三方网站下载现成的
* 官方源码自己编译而成

此处采用第二种方式

1. 官方文档

GitHub仓库：[https://github.com/apache/rocketmq-externals]

中文文档：[https://github.com/apache/rocketmq-externals/blob/master/rocketmq-console/doc/1_0_0/UserGuide_CN.md]

2. 下载源码

源码下载地址：[https://codeload.github.com/apache/rocketmq-externals/zip/master]

3. 配置修改

修改rocketmq-console\src\main\resources\application.properties文件的server.port就欧了，默认8080

4. 编译打包

进入rocketmq-console然后使用maven进行编译打包，直接启动生成的jar包即可（java -jar）

5. 启动控制台

将编译好的SpringBoot程序放到服务器上，然后启动jar包（后台启动可使用nohup &）

### 测试

RocketMQ提供了测试工具和测试类，安装完成之后可以很方便的进行测试

## 4. 常见面试题

### 消息丢失

三种情况下的消息丢失：

* 生成阶段：Producer通过网络将消息发送给Broker，这个发送可能会发生丢失，比如网络延迟不可达等
* 存储阶段：Broker肯定是先把消息放到内存的，然后根据刷盘策略持久化到硬盘中，刚收到Producer的消息，再内存中了，但是异常宕机了，
导致消息丢失
* 消费阶段：消费失败了其实也是消息丢失的一种变体吧

#### Producer生产阶段

1. 同步发送

2. 自动重试机制

默认同步发送，默认重试次数为3次，其他则为1次

3. 多Master多Slave同步双写模式（或者异步复制模式）

#### Broker存储阶段

1. MQ持久化消息分为两种，同步刷盘和异步刷盘，默认情况是异步刷盘，即Broker收到消息之后先存在Cache中再通知Producer消息
存储成功，然后Broker拉起一个异步线程去持久化消息到磁盘中，如果Broker此时宕机的话，消息就会丢失

修改磁盘策略为同步刷盘：flushDiskType=SYNC_FLUSH，异步刷盘默认10S执行一次

2. 集群模式，主从部署，高可用，即设置主从之间同步刷盘之后再通知

```properties
# master节点配置
flushDiskType=SYNC_FLUSH
brokerRole=SYNC_MASTER
```
```properties
# slave节点配置
brokerRole=slave
flushDiskType=SYNC_FLUSH
```

#### Consumer消费阶段

1. 消费者拉取消息，执行完业务逻辑之后，手动进行ACK确认

2. 消息消费失败后，自动重试，默认重试策略和次数为15次

#### 消息持久化

RocketMQ消息持久化在磁盘，默认在/root/store/commitlog文件夹中，文件夹下每个文件大小为1G

CommitLog：消息真正的存储文件
ConsumerQueue：消息消费逻辑队列
IndexFile：消息索引文件，主要存储消息Key和Offset对应关系，提升消息检索速度

关键类：

MappedFile：对应具体的commitlog文件
MappedFileQueue：MappedFile所在的文件夹，对MappedFile封装成文件队列
CommitLog：针对MappedFileQueue的封装使用

CommitLog的内部类：GroupCommitService（同步刷盘，刷完盘才会返回消息写入成功）、FlushRealTimeService（异步刷盘&&关闭字节缓冲区）、
CommitRealTimeService（异步刷盘&&开启字节缓冲区）

CommitLog.submitFlushRequest方法

ServiceThread.wakeup方法以及ServiceThread.waitForRunning方法

#### Queue队列选择算法

两种方式：直接发布消息，Client内部自带选择Queue算法，不允许改变；还有一种是可以自定义Queue的选择算法（内置三种算法）

内置三种算法：

> submitFlushRequest
> SelectMessageQueueByHash
> SelectMessageQueueByMachineRoom
> 自定义实现MessageQueueSelector接口

策略模式：该模式定义了一系列算法，并将每个算法封装起来，使他们可以相互替换，且算法的变化不会影响使用的用户；策略模式属于对象行为模式，它通过对
算法进行封装，把使用算法的责任和算法的实现分割开来，并委派给不同的对象对这些算法进行管理

## 搭建Dledger

主备自动切换

官方手册：[https://github.com/apache/rocketmq/blob/master/docs/cn/dledger/deploy_guide.md]











