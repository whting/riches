package cn.jbricks.module.kafka.consumer.impl;

import cn.jbricks.module.kafka.client.impl.ConsumerClientImpl;
import cn.jbricks.module.kafka.model.Message;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import kafka.consumer.ConsumerIterator;
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
 * @Date: Created in 下午2:21 2018/2/28
 */
public class PartitionConsumer extends KafkaConsumer {


    private static Logger logger = LoggerFactory.getLogger(ConsumerClientImpl.class);

    // 给线程取名
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("PartitionConsumer-%d").setDaemon(true).build();

    private ExecutorService executor;

    @PostConstruct
    public void init() {
        executor = Executors.newFixedThreadPool(consumerConfig.getThreadCount(), threadFactory);

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
        topicCountMap.put(getKafkaTopicWithPrefix(), consumerConfig.getThreadCount());
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getKafkaTopicWithPrefix());

        // 获取Kafka消息流
        for (final KafkaStream stream : streams) {
            executor.submit(new PartitionConsumer.KafkaConsumerThread(stream));
        }
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
