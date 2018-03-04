package cn.jbricks.module.kafka.handler;

import cn.jbricks.module.kafka.handle.AbstractConsumerHandler;
import cn.jbricks.module.kafka.model.Message;
import cn.jbricks.module.kafka.model.User;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午3:07 2018/2/27
 */
public class UserConsumerHandler extends AbstractConsumerHandler<User> {


    @Override
    public void consumer(Message<User> message) {
        User model = message.getModel();
        System.out.println(message.getKey());
        throw new RuntimeException();
    }

}
