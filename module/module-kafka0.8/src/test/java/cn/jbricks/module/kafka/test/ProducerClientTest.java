package cn.jbricks.module.kafka.test;

import cn.jbricks.module.kafka.client.ConsumerClient;
import cn.jbricks.module.kafka.client.ProducerClient;
import cn.jbricks.module.kafka.model.Message;
import cn.jbricks.module.kafka.model.User;
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
    private ProducerClient producerClient;


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
        message.setKey("123");
        message.setMsgId("456");
        User user = new User();
        user.setName("wht");
        message.setModel(user);
        for(int i = 0;i<100;i++){
            producerClient.sendMessage(message);
        }
        Thread.sleep(10000L);
    }


}
