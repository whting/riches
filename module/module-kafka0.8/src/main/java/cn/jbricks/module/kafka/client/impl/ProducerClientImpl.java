package cn.jbricks.module.kafka.client.impl;

import cn.jbricks.module.kafka.client.ProducerClient;
import cn.jbricks.module.kafka.config.ProducerConfig;
import cn.jbricks.module.kafka.model.Message;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public class ProducerClientImpl implements ProducerClient {


    public static final Logger logger = LoggerFactory.getLogger(ProducerClientImpl.class);

    private String topic;

    private ProducerConfig producerConfig;

    private Producer producer;

    private Random random = new SecureRandom();

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("metadata.broker.list", producerConfig.getBrokerList());
        props.put("request.required.acks", "1");
        props.put("producer.type", producerConfig.getProducerType());
        props.put("compression.codec", "0");
        props.put("batch.num.messages", "20");
        props.put("queue.buffering.max.messages", "1000");
        props.put("send.buffer.bytes", "102400");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.NullEncoder");
        props.put("queue.buffering.max.ms", "50");
        props.put("num.partitions", "5");
        kafka.producer.ProducerConfig config = new kafka.producer.ProducerConfig(props);
        producer = new Producer<>(config);
    }

    @PreDestroy
    public void shutdown() {
        producer.close();
    }

    @Override
    public boolean sendMessage(Message message) {
        byte[] bytes = null;
        try {
            bytes = producerConfig.getMessageConverter().toByte(message);
        } catch (UnsupportedEncodingException e) {
            logger.error("[sendMessage]message convert to byte error", e);
            return false;
        }
        String key = String.valueOf(random.nextLong());
        KeyedMessage<String, String> keyedMessage = new KeyedMessage(getKafkaTopicWithPrefix(), key, bytes);
        producer.send(keyedMessage);

        return true;
    }

    private String getKafkaTopicWithPrefix() {
        String prefix = producerConfig.getTopicPrefix();
        return prefix + topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }
}
