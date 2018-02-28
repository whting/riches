package cn.jbricks.module.kafka.consumer.impl;

import cn.jbricks.module.kafka.client.impl.ConsumerClientImpl;
import cn.jbricks.module.kafka.model.Message;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:22 2018/2/28
 */
public class AsyncConsumer extends KafkaConsumer implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ConsumerClientImpl.class);

    private KafkaStream<byte[], byte[]> kafkaStream;

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("PartitionConsumer-%d").setDaemon(true).build();

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), threadFactory);;

    @PostConstruct
    public void init() {

        Properties props = new Properties();
        props.put("zookeeper.connect", consumerConfig.getZookeeperHost());
        props.put("group.id", consumerConfig.getGroupId());
        props.put("zookeeper.session.timeout.ms", consumerConfig.getZkSessionTimeout());
        // props.put("auto.commit.interval.ms", "1000");  //定期提交offset，默认10000
        // props.put("zookeeper.sync.time.ms", consumerConfig.getZkSyncTimeMs());
        kafka.consumer.ConsumerConfig config = new kafka.consumer.ConsumerConfig(props);

        // 连接Kafka
        consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(config);
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(getKafkaTopicWithPrefix(), 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getKafkaTopicWithPrefix());

        kafkaStream = streams.get(0);

        new Thread(this, "Worker-" + this.getClass().getName()).start();
    }

    @Override
    public void run() {
        // 拉取消息
        for (MessageAndMetadata<byte[], byte[]> e : kafkaStream) {
            byte[] message = e.message();

            // 空的消息就丢掉
            if (message == null || message.length == 0) {
                logger.warn("Received empty message, ignoring");
                continue;
            }
            executor.execute(new KafkaConsumerThread(message));
        }
    }

    private class KafkaConsumerThread implements Runnable {

        private byte[] message;

        public KafkaConsumerThread(byte[] message) {
            this.message = message;
        }

        @Override
        public void run() {

            Message msg = null;
            try {
                ParameterizedType parameterizedType = (ParameterizedType) consumerHandler.getClass().getGenericSuperclass();
                msg = (Message) consumerConfig.getMessageConverter().toObject(message, (Class) parameterizedType.getActualTypeArguments()[0]);
            } catch (IOException e) {
                logger.error("[consume]message convert to object error", e);
            }
            onMessage(msg);
        }

    }
}
