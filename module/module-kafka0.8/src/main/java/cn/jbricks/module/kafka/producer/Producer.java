package cn.jbricks.module.kafka.producer;

import cn.jbricks.module.kafka.model.Message;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午11:55 2018/2/28
 */
public interface Producer {

    void init();

    void shutdown();

    boolean sendMessage(Message message);

    boolean sendMessage(Message message,String key);
}
