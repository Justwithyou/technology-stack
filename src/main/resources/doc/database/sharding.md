# 数据库中间件

## 分库分表中间件（Sharding）

### ShardingSphere

官网：[http://shardingsphere.apache.org/index_zh.html]

文档：[https://shardingsphere.apache.org/document/current/cn/overview/]

相关博文：[https://blog.csdn.net/u013308490/article/details/94598606]

### MyCat

官网：[http://www.mycat.org.cn/]

相关博文：[https://www.cnblogs.com/kingsonfu/p/10627802.html]

### 小米Gaea

Github：[https://github.com/XiaoMi/Gaea]

### NewSQL

## 数据同步

### MySQL数据同步到ElasticSearch

相关博文：[https://www.cnblogs.com/yulibostu/articles/12530901.html]

### Canal

Github：[https://github.com/alibaba/canal]

相关博文：[https://blog.csdn.net/yehongzhi1994/article/details/107880162]

## 雪花算法

SnowFlake：分布式id生成算法，使用一个64位的long型数字作为全局唯一id

第一部分：1位，0，无任何意义，在二进制中首尾为0表示正数
第二部分：41位，表示时间戳，单位毫秒
第三部分：5位，工作机房id，最多代表32个机房
第四部分：5位，工作机器id，最多代表每个机房32台机器
第五部分：12位，同一毫秒内产生的不同id