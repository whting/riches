package cn.jbricks.module.kafka.consumer;

import cn.jbricks.module.kafka.model.Message;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午11:55 2018/2/28
 */
public interface Consumer<T> {

    void onMessage(Message<T> message);
}
