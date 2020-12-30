package com.technology.stack.spring.kafka.util;

import kafka.admin.AdminClient;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.api.LeaderAndIsr;
import kafka.cluster.Broker;
import kafka.cluster.Cluster;
import kafka.coordinator.group.GroupOverview;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import kafka.zk.ZkSecurityMigratorUtils;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooKeeper;
import scala.Option;
import scala.collection.JavaConversions;

import java.io.IOException;
import java.util.*;

/**
 * Kafka处理工具类
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/28 16:54
 */
public class KafkaUtil {

    private final ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
    private ZooKeeper zooKeeper = null;

    /**
     * 获取集群信息
     *
     * @return Cluster
     */
    public Cluster getCluster() {

        return zkUtils.getCluster();
    }

    /**
     * 获取领导者及分区同步副本
     *
     * @param topicName 主题
     * @param partition 分区序号
     */
    public void getLeaderAndIsrForPartition(String topicName, int partition) {

        Option<LeaderAndIsr> res = zkUtils.getLeaderAndIsrForPartition(topicName, partition);
        System.out.println("获取领导者以及分区同步副本：" + res);
    }

    /**
     * 创建消费者
     *
     * @param groupId 消费者组
     * @param topic 主题
     * @return boolean
     */
    public boolean createConsumer(String groupId, String topic) {

        /*try {
            Properties properties = new Properties();
            properties.put("zookeeper.connect", "localhost:2181");
            properties.put("group.id", groupId);

            ConsumerConnector consumer = new ZookeeperConsumerConnector(new ConsumerConfig(properties),true);
            java.util.Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
            if (topic != null && !"".equals(topic)){
                topicCountMap.put(topic, 1); // 一次从主题中获取一个数据
            }else {
                topicCountMap.put("topic", 1); // 一次从主题中获取一个数据
            }
            Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
            return true;
        } catch(RuntimeException e) {
            return false;
        }*/

        return false;
    }

    /**
     * 删除Topic路径
     *
     * @return String
     */
    public String deleteTopicPath() {
        return zkUtils.DeleteTopicsPath();
    }

    /**
     * 获取Broker信息
     *
     * @param brokerId brokerId
     * @return Broker
     */
    public Broker getBrokerInfo(int brokerId) {
        return zkUtils.getBrokerInfo(brokerId).get();
    }

    /**
     * 获取消费者的路径
     *
     * @return String
     */
    public  String ConsumersPath(){
        return zkUtils.ConsumersPath();
    }
    /**
     * 删除多个topic
     *
     * @param topicNames 主题列表
     * @return String[]
     */
    public  String[] deleteTopics(final String...topicNames) {

        if (topicNames == null || topicNames.length == 0) {
            return new String[0];
        }
        Set<String> deleted = new LinkedHashSet<>();
        for (String topicName : topicNames) {
            if (topicName != null || !topicName.trim().isEmpty()) {
                deleteTopic(topicName);
                deleted.add(topicName.trim());
            }
        }
        return deleted.toArray(new String[deleted.size()]);
    }

    /**
     * 获取所有的TopicList
     *
     * @return List<String>
     */
    public List<String> getTopicList() {
        return JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
    }
    /**
     * 获取某个分组下的所有消费者
     *
     * @param groupName 消费者组
     * @return List<String>
     */
    public List<String> getConsumersInGroup(String groupName) {
        // return JavaConversions.seqAsJavaList(zkUtils.getConsumersInGroup(groupName));
        return null;
    }
    /**
     * 判断某个topic是否存在
     *
     * @param topicName 主题
     * @return boolean
     */
    public  boolean topicExists(String topicName) {
        return AdminUtils.topicExists(zkUtils,topicName);
    }
    /**
     * 消费者组是否活跃
     *
     * @param groupName 消费者组名
     * @return boolean
     */
    public boolean isConsumerGroupActive(String groupName) {
        // return AdminUtils.isConsumerGroupActive(zkUtils,groupName);
        return false;
    }
    /**
     * 获取所有消费者组
     *
     * @return List<String>
     */
    public List<String> getConsumerGroups() {
        // return JavaConversions.seqAsJavaList(zkUtils.getConsumerGroups());
        return null;
    }
    /**
     * 根据消费者的名称获取topic
     *
     * @param groupName 消费者组
     * @return List<String>
     */
    public List<String> getTopicsByConsumerGroup(String groupName) {
        // return JavaConversions.seqAsJavaList(zkUtils.getTopicsByConsumerGroup(groupName));
        return null;
    }

    /**
     * 获取排序的BrokerList
     *
     * @return List<Object>
     */
    public List<Object> getSortedBrokerList() {
        return JavaConversions.seqAsJavaList(zkUtils.getSortedBrokerList());
    }
    public  List<Broker>   getAllBrokersInCluster() {
        return JavaConversions.seqAsJavaList(zkUtils.getAllBrokersInCluster());
    }

    /**
     * 获取消费某个topic发送消息的消费组
     *
     * @param topicName 主题
     * @return Set<String>
     */
    public Set<String> getAllConsumerGroupsForTopic(String topicName) {
        // return zkUtils.getAllConsumerGroupsForTopic(topicName);
        return null;
    }
    /**
     * 获取删除主题的路径
     *
     * @param topicName 主题
     * @return String
     */
    public String getDeleteTopicPath(String topicName) {
        return zkUtils.getDeleteTopicPath(topicName);
    }

    /**
     * 获取topic路径
     *
     * @param topicName 主题
     * @return String
     */
    public String getTopicPath(String topicName) {
        return zkUtils.getTopicPath(topicName);
    }

    public  boolean createTopic(String topicName) {
        try {
            AdminUtils.createTopic(zkUtils, topicName, 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
            return  true;
        } catch (RuntimeException exception){
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 删除topic信息（前提是server.properties中要配置delete.topic.enable=true）
     *
     * @param topicName 主题
     */
    public  void deleteTopic(String topicName){
        // 删除topic 't1'
        AdminUtils.deleteTopic(zkUtils, topicName);
        System.out.println("删除成功！");
    }

    /**
     *  删除topic的某个分区
     *
     * @param brokerId brokerId
     * @param topicName 主题
     */
    public void deletePartition(int brokerId, String topicName) {
        // 删除topic 't1'
        zkUtils.deletePartition(brokerId, topicName);
        System.out.println("删除成功！");
    }


    /**
     * 改变topic的配置
     *
     * @param topicName 主题
     */
    public  void updateTopic(String topicName){

        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
        // 增加topic级别属性
        props.put("min.cleanable.dirty.ratio", "0.3");
        // 删除topic级别属性
        props.remove("max.message.bytes");
        props.put("retention.ms","1000");
        // 修改topic 'test'的属性
        AdminUtils.changeTopicConfig(zkUtils, "test", props);
        System.out.println("修改成功");
        zkUtils.close();
    }

    /**
     * 获取所有topic的配置信息
     */
    @SuppressWarnings("unchecked")
    public  Map<String, Properties> fetchAllTopicConfigs(){
        return (Map<String, Properties>) AdminUtils.fetchAllTopicConfigs(zkUtils);
    }
    /**
     * 获取所有topic或者client的信息()type为：ConfigType.Topic()/ConfigType.Client()
     */
    @SuppressWarnings("unchecked")
    public  Map<String, Properties> fetchAllEntityConfigs(String type){
        return (Map<String, Properties>) AdminUtils.fetchAllEntityConfigs(zkUtils, type);
    }
    /**
     * 获取指定topic的配置信息
     *
     * @param topicName 主题
     */
    public  Properties fetchEntityConfig(String topicName){

        return AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topicName);
    }

    private boolean deleteUselessConsumer(String topic, String group) {
        String sb = "/consumers/" +
                group;
        return recursivelyDeleteData(sb);
    }

    private boolean recursivelyDeleteData(String path) {
        List<String> childList = getChildrenList(path);
        if (childList == null) {
            return false;
        } else if (childList.isEmpty()) {
            deleteData(path);
        } else {
            for (String childName : childList) {
                String childPath = path + "/" + childName;
                List<String> grandChildList = getChildrenList(childPath);
                if (grandChildList == null) {
                    return false;
                } else if (grandChildList.isEmpty()) {
                    deleteData(childPath);
                } else {
                    recursivelyDeleteData(childPath);
                }
            }
            deleteData(path);
        }
        return true;
    }

    private boolean deleteData(String path) {
        try {
            zooKeeper.delete(path, -1);
        } catch (InterruptedException | KeeperException e) {
            return false;
        }

        return true;
    }

    private List<String> getChildrenList(String path) {

        try {
            zooKeeper = new ZooKeeper("localhost:2181", 6000, null);
            return zooKeeper.getChildren(path, false, null);
        } catch (KeeperException | InterruptedException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>(Collections.singleton(path));
    }

    /**
     * get all subscribing consumer group names for a given topic
     *
     * @param brokerListUrl localhost:9092 for instance
     * @param topic         topic name
     * @return Set<String>
     */
    public static Set<String> getAllGroupsForTopic(String brokerListUrl, String topic) {
        AdminClient client = AdminClient.createSimplePlaintext(brokerListUrl);
        try {
            List<GroupOverview> allGroups = scala.collection.JavaConversions.seqAsJavaList(client.listAllGroupsFlattened().toSeq());
            java.util.Set<String> groups =  new HashSet<String>();
            for (GroupOverview overview: allGroups) {
                String groupID = overview.groupId();
                java.util.Map<TopicPartition, Object> offsets = JavaConversions.mapAsJavaMap(client.listGroupOffsets(groupID));
                groups.add(groupID);
            }
            return groups;
        } finally {
            client.close();
        }
    }
}
