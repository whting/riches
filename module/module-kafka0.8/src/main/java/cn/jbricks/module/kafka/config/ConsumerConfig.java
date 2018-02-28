package cn.jbricks.module.kafka.config;

import cn.jbricks.module.kafka.convert.MessageConverter;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public class ConsumerConfig {

    private String  topicPrefix      = "";

    private String  zookeeperHost;

    private String  groupId;

    private String  zkSessionTimeout = "1000";

    private String  zkSyncTimeMs     = "1000";

    private int threadCount = 1;

    private MessageConverter messageConverter;

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public void setZookeeperHost(String zookeeperHost) {
        this.zookeeperHost = zookeeperHost;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public void setZkSessionTimeout(String zkSessionTimeout) {
        this.zkSessionTimeout = zkSessionTimeout;
    }

    public String getZkSyncTimeMs() {
        return zkSyncTimeMs;
    }

    public void setZkSyncTimeMs(String zkSyncTimeMs) {
        this.zkSyncTimeMs = zkSyncTimeMs;
    }

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
