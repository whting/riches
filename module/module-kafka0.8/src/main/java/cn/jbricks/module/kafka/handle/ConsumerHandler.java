package cn.jbricks.module.kafka.handle;


import cn.jbricks.module.kafka.model.Message;

/**
 * 消息处理器
 * <p>
 * Created by haoting.wang on 2017/2/27.
 */
public interface ConsumerHandler<T> {

    /**
     * 消息处理
     *
     * @param message
     */
    void consumer(Message<T> message);

    /**
     * 是否重试
     *
     * @param count
     * @return
     */
    boolean isRetry(int count);

    /**
     * 消息消费失败重试
     *
     * @param message
     * @return
     */
    boolean retry(Message message);

}
