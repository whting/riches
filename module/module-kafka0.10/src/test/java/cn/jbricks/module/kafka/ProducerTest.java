package cn.jbricks.module.kafka;

import cn.jbricks.module.kafka.producer.config.ProducerConfig;
import cn.jbricks.module.kafka.message.KafkaMessage;
import cn.jbricks.module.kafka.serializer.JsonMessageSerializer;
import org.junit.Test;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午11:00 2018/5/5
 */
public class ProducerTest {


    @Test
    public void test() throws Exception {
        ProducerConfig config = new ProducerConfig();
        config.setServers("localhost:9092");
        config.setValueSerializer(JsonMessageSerializer.class.getName());

        TopicProducerProvider producerProvider = new TopicProducerProvider();
        producerProvider.setConfig(config);
        producerProvider.afterPropertiesSet();

        KafkaMessage message = new KafkaMessage("1","hello world");
        producerProvider.publish("test",message);

        Thread.sleep(1000l);
    }
}
