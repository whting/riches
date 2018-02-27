package cn.jbricks.module.kafka.client.impl;

import cn.jbricks.module.kafka.client.ProducerClient;
import cn.jbricks.module.kafka.config.ProducerConfig;
import cn.jbricks.module.kafka.model.Message;
import com.alibaba.fastjson.JSONObject;
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
        String body = null;
       // String s = JSONObject.toJSONString(message);
        try {
            body = producerConfig.getMessageConverter().toString(message);
        } catch (UnsupportedEncodingException e) {
            logger.error("[sendMessage]message convert to byte error", e);
            return false;
        }
        String key = String.valueOf(random.nextLong());
        KeyedMessage<String, String> keyedMessage = new KeyedMessage(getKafkaTopicWithPrefix(), key, body);
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
