package cn.jbricks.module.kafka.producer.impl;

import cn.jbricks.module.kafka.config.ProducerConfig;
import cn.jbricks.module.kafka.model.Message;
import cn.jbricks.module.kafka.producer.Producer;
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
 * @Author: haoting.wang
 * @Date: Created in 上午11:58 2018/2/28
 */
public class KafkaProducer implements Producer {

    public static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private kafka.javaapi.producer.Producer producer;

    private Random random = new SecureRandom();

    private ProducerConfig producerConfig;

    private String topic;

    @Override
    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("metadata.broker.list", producerConfig.getBrokerList());
        props.put("request.required.acks", "1"); //0：立即返回   1：主服务器收到消息   -1：所有服务器收到返回
        props.put("producer.type", producerConfig.getProducerType());   //同步／异步

        props.put("batch.num.messages", "20");  //指定每次批量发送数据量，默认为200
        props.put("queue.buffering.max.messages", "1000");  //producer端允许buffer的最大消息量,默认为10000
        props.put("queue.buffering.max.ms", "50"); //当message被缓存的时间超过此值后,将会批量发送给broker,默认为5000ms
        props.put("queue.enqueue.timeout.ms", "-1"); //-1: 无阻塞超时限制,消息不会被抛弃 0:立即清空队列,消息被抛弃

        props.put("send.buffer.bytes", "102400");
        props.put("compression.codec", "0");    //压缩
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.NullEncoder");
        kafka.producer.ProducerConfig config = new kafka.producer.ProducerConfig(props);
        producer = new kafka.javaapi.producer.Producer(config);
    }

    @Override
    @PreDestroy
    public void shutdown() {
        producer.close();
    }


    @Override
    public boolean sendMessage(Message message) {

        String key = String.valueOf(random.nextLong());
        return sendMessage(message,key);
    }

    @Override
    public boolean sendMessage(Message message, String key) {

        String body = null;
        try {
            body = producerConfig.getMessageConverter().toString(message);
        } catch (UnsupportedEncodingException e) {
            logger.error("[sendMessage]message convert to byte error", e);
            return false;
        }
        KeyedMessage<String, String> keyedMessage = new KeyedMessage(getKafkaTopicWithPrefix(), body);

        producer.send(keyedMessage);

        return true;
    }

    private String getKafkaTopicWithPrefix() {
        String prefix = producerConfig.getTopicPrefix();
        return prefix + topic;
    }


    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
