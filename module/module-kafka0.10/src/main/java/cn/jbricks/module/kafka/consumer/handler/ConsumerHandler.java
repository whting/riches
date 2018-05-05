package cn.jbricks.module.kafka.consumer.handler;

import cn.jbricks.module.kafka.message.KafkaMessage; /**
 * @Author: haoting.wang
 * @Date: Created in 下午1:22 2018/5/5
 */
public interface ConsumerHandler {
    void p1Process(KafkaMessage message);

    void p2Process(KafkaMessage message);

    boolean onProcessError(KafkaMessage message);
}
