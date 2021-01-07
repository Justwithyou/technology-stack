# Flink 

Flink官网：[https://flink.apache.org]

Flink文档：[https://ci.apache.org/projects/flink/]

## 基础概念

Flink是一款分布式的计算引擎，它可以用来做批处理，即处理静态的数据集、历史的数据集；也可以用来做流处理，即实时的处理一些实时数据流，
实时的产生数据的结果；也可以用来做一些基于事件的应用

技术博文：[https://blog.csdn.net/m0_37803704/article/details/86563457]

Flink：在数据流上的有状态的计算

数据流：有界数据集、无界数据流

Flink认为有界数据集是无界数据流的一个特例，所以说有界数据集也是一种数据流，事件流也是一种数据流，Flink可以支持处理任何的数据，
可以支持批处理、流处理、AI等

有状态的计算：Flink提供了内置的对状态的一致性的处理方式，即如果任务发生了FailOver，其状态不会丢失、不会被多算少算，同时提供了
非常高的性能

Flink包括高性能、高可扩展性、支持容错，是一种纯内存式的计算引擎，做了内存管理方面的大量优化，另外也支持eventime的处理、支持
超大状态的Job、支持exactly-once的处理

### Flink基石

Checkpoint、State、Time、Window

1. Checkpoint机制