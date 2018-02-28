package cn.jbricks.module.kafka.test;

import cn.jbricks.module.kafka.model.Message;
import cn.jbricks.module.kafka.model.User;
import cn.jbricks.module.kafka.producer.Producer;
import cn.jbricks.module.kafka.producer.impl.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by haoting.wang on 2017/2/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:kafka.xml")
public class ProducerClientTest {

    @Autowired
    private Producer producerClient;


    @Test
    public void testProducer1() throws InterruptedException {
        Message message = new Message();
        message.setKey("123");
        message.setMsgId("456");
        User user = new User();
        user.setName("wht");
        message.setModel(user);

        producerClient.sendMessage(message);

        Thread.sleep(1000L);
    }

    @Test
    public void testProducer2() throws InterruptedException {
        Message message = new Message();
        message.setMsgId("456");
        User user = new User();
        message.setModel(user);
        for(int i = 0;i<1000;i++){
            message.setKey(i+"");

            user.setName(i+"");
            producerClient.sendMessage(message);
        }
        Thread.sleep(3000L);
    }


}
