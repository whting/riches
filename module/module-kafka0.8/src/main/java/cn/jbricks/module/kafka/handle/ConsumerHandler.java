package cn.jbricks.module.kafka.handle;


import cn.jbricks.module.kafka.model.Message;

/**
 * Created by haoting.wang on 2017/2/27.
 */
public interface ConsumerHandler<T> {

    public void consumer(Message<T> message);

    public abstract Message<T> parseByte(byte[] data);


}
