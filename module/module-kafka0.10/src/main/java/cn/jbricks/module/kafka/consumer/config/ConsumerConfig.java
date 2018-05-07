package cn.jbricks.module.kafka.consumer.config;

import cn.jbricks.module.kafka.serializer.JsonMessageDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午1:04 2018/5/5
 */
public class ConsumerConfig {


    private String servers;

    private String topicPrefix;

    private boolean autoCommit = true;

    private int rebalanceMaxRetries = 5;

    private long rebalanceBackoffMs = 1205;

    private long zookeeperSessionTimeoutMs = 6000;

    private String keyDeserializer = StringDeserializer.class.getName();

    private String valueDeserializer = JsonMessageDeserializer.class.getName();

    private String autoOffsetReset = "earliest";

    private int maxPollRecords = 1000;

    private String groupId;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public int getRebalanceMaxRetries() {
        return rebalanceMaxRetries;
    }

    public void setRebalanceMaxRetries(int rebalanceMaxRetries) {
        this.rebalanceMaxRetries = rebalanceMaxRetries;
    }

    public long getRebalanceBackoffMs() {
        return rebalanceBackoffMs;
    }

    public void setRebalanceBackoffMs(long rebalanceBackoffMs) {
        this.rebalanceBackoffMs = rebalanceBackoffMs;
    }

    public long getZookeeperSessionTimeoutMs() {
        return zookeeperSessionTimeoutMs;
    }

    public void setZookeeperSessionTimeoutMs(long zookeeperSessionTimeoutMs) {
        this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
