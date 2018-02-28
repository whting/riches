package cn.jbricks.module.kafka.consumer.impl;

import cn.jbricks.module.kafka.model.Message;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * kafka消费者，此消费者先拉取一定量的数据缓存（当数据量达到bufferSize，或者时间间隔为flushInterval）交给异步线程处理。推荐使用
 *
 * @Author: haoting.wang
 * @Date: Created in 下午3:12 2018/2/28
 */
public class BulkConsumer extends KafkaConsumer implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(AsyncConsumer.class);

    private KafkaStream<byte[], byte[]> kafkaStream;

    private List<byte[]> buffer = new ArrayList<>();

    private int bufferSize = 200;       //消息缓存数

    private Timer timer;

    private long flushInterval = 50l;   //时间间隔

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
        topicCountMap.put(getKafkaTopicWithPrefix(), 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getKafkaTopicWithPrefix());

        kafkaStream = streams.get(0);

        new Thread(this, "Worker-" + this.getClass().getName()).start();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                asyncFlush();
            }
        }, flushInterval, flushInterval);
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
            bufferAdd(message);
        }
    }

    private void bufferAdd(byte[] message) {
        buffer.add(message);
        if (bufferSize <= buffer.size()) {
            asyncFlush();
        }
    }

    private void asyncFlush() {
        // 切换缓冲区
        if (this.buffer.isEmpty()) {
            return;
        }
        final List<byte[]> dataBuffer = this.buffer;
        this.buffer = new ArrayList<>(buffer.size());
        executor.submit(new BulkConsumer.KafkaConsumerThread(dataBuffer));
    }

    public void onMessage(List<Message> messages) {

        for (Message message : messages) {
            onMessage(message);
        }
    }

    private class KafkaConsumerThread implements Runnable {

        private List<byte[]> messageBytes;

        public KafkaConsumerThread(List<byte[]> messageBytes) {
            this.messageBytes = messageBytes;
        }

        @Override
        public void run() {

            List<Message> messageList = new ArrayList<>(messageBytes.size());

            for (byte[] message : messageBytes) {
                Message msg = null;
                try {
                    ParameterizedType parameterizedType = (ParameterizedType) consumerHandler.getClass().getGenericSuperclass();
                    msg = (Message) consumerConfig.getMessageConverter().toObject(message, (Class) parameterizedType.getActualTypeArguments()[0]);
                } catch (IOException e) {
                    logger.error("[consume]message convert to object error", e);
                }
                messageList.add(msg);
            }

            onMessage(messageList);
        }
    }


    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public long getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(long flushInterval) {
        this.flushInterval = flushInterval;
    }
}
