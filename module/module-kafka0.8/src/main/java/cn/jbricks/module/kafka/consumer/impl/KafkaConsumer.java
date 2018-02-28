package cn.jbricks.module.kafka.consumer.impl;

import cn.jbricks.module.kafka.config.ConsumerConfig;
import cn.jbricks.module.kafka.consumer.Consumer;
import cn.jbricks.module.kafka.handle.ConsumerHandler;
import cn.jbricks.module.kafka.model.Message;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import kafka.javaapi.consumer.ConsumerConnector;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午11:56 2018/2/28
 */
public abstract class KafkaConsumer implements Consumer {

    protected String topic;

    protected ConsumerConfig consumerConfig;

    protected ConsumerConnector consumerConnector;

    protected ConsumerHandler consumerHandler;

    protected ExecutorService executor;

    // 给线程取名
    protected ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(this.getClass().getSimpleName() + "-%d").setDaemon(true).build();

    @PreDestroy
    public void shutdown() {
        consumerConnector.shutdown();
        executor.shutdownNow();
    }

    public void onMessage(Message message) {
        try {
            consumerHandler.consumer(message);
        } catch (Exception e) {
            consumerHandler.retry(message);
        }
    }

    public String getKafkaTopicWithPrefix() {
        String prefix = consumerConfig.getTopicPrefix();
        return prefix + topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public ConsumerHandler getConsumerHandler() {
        return consumerHandler;
    }

    public void setConsumerHandler(ConsumerHandler consumerHandler) {
        this.consumerHandler = consumerHandler;
    }
}

