package cn.jbricks.module.kafka.config;

import cn.jbricks.module.kafka.convert.MessageConverter;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public class ProducerConfig {

    private String brokerList;
    //区分环境
    private String topicPrefix = "";
    private String producerType = "async";

    private MessageConverter messageConverter;


    public String getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getProducerType() {
        return producerType;
    }

    public void setProducerType(String producerType) {
        this.producerType = producerType;
    }


    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}


