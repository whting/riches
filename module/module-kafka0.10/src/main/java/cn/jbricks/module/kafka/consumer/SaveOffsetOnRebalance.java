package cn.jbricks.module.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class SaveOffsetOnRebalance implements ConsumerRebalanceListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOffsetOnRebalance.class);

    private Consumer consumer;

    private Map<TopicPartition, OffsetAndMetadata> uncommittedOffsetMap;

    //初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
    public SaveOffsetOnRebalance(Consumer consumer, Map<TopicPartition, OffsetAndMetadata> uncommittedOffsetMap) {
        this.consumer = consumer;
        this.uncommittedOffsetMap = uncommittedOffsetMap;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

        logger.info("kafka in ralance:onPartitionsRevoked");
        while (!uncommittedOffsetMap.isEmpty()) {
            consumer.commitSync(uncommittedOffsetMap);
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        //rebalance之后 获取新的分区，获取最新的偏移量，设置拉取分量

        logger.info("kafka in ralance:onPartitionsAssigned  ");

        for (TopicPartition partition : collection) {

            //获取消费偏移量，实现原理是向协调者发送获取请求
            OffsetAndMetadata offset = consumer.committed(partition);
            //设置本地拉取分量，下次拉取消息以这个偏移量为准
            consumer.seek(partition, offset.offset());
        }
    }
}