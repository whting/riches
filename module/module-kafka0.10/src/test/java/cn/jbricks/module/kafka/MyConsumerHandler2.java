package cn.jbricks.module.kafka;

import cn.jbricks.module.kafka.consumer.handler.ConsumerHandler;
import cn.jbricks.module.kafka.message.KafkaMessage;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:25 2018/5/5
 */
public class MyConsumerHandler2 implements ConsumerHandler {
    @Override
    public void p1Process(KafkaMessage message) {
        System.out.println("MyConsumerHandler2 p1Process");
    }

    @Override
    public void p2Process(KafkaMessage message) {

        System.out.println("MyConsumerHandler2 p2Process");
    }

    @Override
    public boolean onProcessError(KafkaMessage message) {

        System.out.println("MyConsumerHandler2 onProcessError");
        return false;
    }
}
