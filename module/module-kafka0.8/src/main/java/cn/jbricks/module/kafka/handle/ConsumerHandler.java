package cn.jbricks.module.kafka.handle;


import cn.jbricks.module.kafka.model.Message;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public interface ConsumerHandler<T> {

    void consumer(Message<T> message);

    boolean isRetry(int count);

    boolean retry(Message message);

}
