package cn.jbricks.module.kafka.producer.handler;

import cn.jbricks.module.kafka.message.KafkaMessage;
import org.apache.kafka.clients.producer.RecordMetadata; /**
 * @Author: haoting.wang
 * @Date: Created in 上午10:15 2018/5/5
 */
public interface ProducerEventHandler {


    void onError(String topicName, KafkaMessage message);

    void onSuccessed(String topicName, RecordMetadata metadata);
}
