package cn.jbricks.module.kafka.test;

import cn.jbricks.module.kafka.client.ConsumerClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午3:12 2018/2/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:kafka.xml")
public class ConsumerClientTest {

    @Autowired
    private ConsumerClient consumerClient;



    public void testConsumer1(){

    }
}
