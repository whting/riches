package cn.jbricks.module.kafka.client.impl;

import cn.jbricks.module.kafka.client.ConsumerClient;
import cn.jbricks.module.kafka.config.ConsumerConfig;
import cn.jbricks.module.kafka.handle.ConsumerHandler;
import cn.jbricks.module.kafka.model.Message;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
 * Created by haoting.wang on 2017/2/27.
 */
public class ConsumerClientImpl implements ConsumerClient {

    private static Logger logger = LoggerFactory.getLogger(ConsumerClientImpl.class);

    private String topic;

    private ConsumerConfig consumerConfig;

    private ConsumerConnector consumerConnector;

    private ConsumerHandler consumerHandler;

    private static int threadCount = Runtime.getRuntime().availableProcessors();
    // 给线程取名
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("consumer-%d").setDaemon(true).build();

    private static ExecutorService executor = Executors.newFixedThreadPool(threadCount, threadFactory);

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("zookeeper.connect", consumerConfig.getZookeeperHost());
        props.put("group.id", consumerConfig.getGroupId());
        props.put("zookeeper.session.timeout.ms", consumerConfig.getZkSessionTimeout());
        // props.put("zookeeper.sync.time.ms", consumerConfig.getZkSyncTimeMs());
        kafka.consumer.ConsumerConfig config = new kafka.consumer.ConsumerConfig(props);

        // 连接Kafka
        consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(config);
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(getKafkaTopicWithPrefix(), threadCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getKafkaTopicWithPrefix());

        // 获取Kafka消息流
        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumerThread(stream));
        }
    }

    @PreDestroy
    public void shutdown() {
        consumerConnector.shutdown();
        executor.shutdownNow();
    }

    private void onMessage(Message message) {
        try {
            consumerHandler.consumer(message);
        } catch (Exception e) {
            consumerHandler.retry(message);
        }
    }

    private String getKafkaTopicWithPrefix() {
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

    private class KafkaConsumerThread implements Runnable {

        private KafkaStream<byte[], byte[]> stream;

        public KafkaConsumerThread(KafkaStream stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                MessageAndMetadata<byte[], byte[]> metadata = it.next();
                byte[] message = metadata.message();
                if (message == null || message.length == 0) {
                    logger.warn("Received empty message, ignoring");
                    return;
                }
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
}
