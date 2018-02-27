package cn.jbricks.module.kafka.client;


import cn.jbricks.module.kafka.model.Message;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public interface ProducerClient {

    void init();

    void shutdown();

    boolean sendMessage(Message message);
}
