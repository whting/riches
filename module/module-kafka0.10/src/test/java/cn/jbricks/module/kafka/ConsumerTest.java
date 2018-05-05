package cn.jbricks.module.kafka;

import cn.jbricks.module.kafka.consumer.config.ConsumerConfig;
import cn.jbricks.module.kafka.consumer.handler.ConsumerHandler;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:25 2018/5/5
 */
public class ConsumerTest {

    @Test
    public void test() throws Exception {

        ConsumerConfig config = new ConsumerConfig();
        config.setServers("localhost:9092");
        config.setGroupId("demo");

        config.setAutoCommit(false);

        TopicConsumerProvider consumerProvider = new TopicConsumerProvider();

        consumerProvider.setConfig(config);


        Map<String,ConsumerHandler> map = new HashMap<>();
        map.put("test",new MyConsumerHandler());
        map.put("test3",new MyConsumerHandler2());
        consumerProvider.setTopicHandlers(map);

        consumerProvider.afterPropertiesSet();

        Thread.sleep(100000000l);

    }
}
