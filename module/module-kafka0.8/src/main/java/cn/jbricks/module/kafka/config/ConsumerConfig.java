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

    //是否开启重试
    private boolean retry            = false;


    private MessageConverter messageConverter;

    public boolean isRetry(int count) {
        if (!retry) {
            return false;
        }
        if (count < 3) {
            return true;
        }
        return false;
    }

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

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }


    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}
