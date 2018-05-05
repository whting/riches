package cn.jbricks.module.kafka.producer.config;


import cn.jbricks.module.kafka.serializer.JsonMessageSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public class ProducerConfig {

    private String servers;
    //区分环境

    private String topicPrefix;

    private String acks = "all";

    private int retries = 3;

    private boolean async = true;

    private int batchSize = 1000;

    private String keySerializer = StringSerializer.class.getName();

    private String valueSerializer = JsonMessageSerializer.class.getName();


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

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}


