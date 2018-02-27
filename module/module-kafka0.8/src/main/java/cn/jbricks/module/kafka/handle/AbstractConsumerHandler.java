package cn.jbricks.module.kafka.handle;

import cn.jbricks.module.kafka.client.impl.ConsumerClientImpl;
import cn.jbricks.module.kafka.model.Message;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午12:28 2018/2/28
 */
public abstract class AbstractConsumerHandler<T> implements ConsumerHandler<T> {


    private static Logger logger = LoggerFactory.getLogger(AbstractConsumerHandler.class);

    private long retryInterval = 500l;


    public boolean isRetry(int count) {
        if (count < 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean retry(Message message) {
        int count = 0;
        while (isRetry(count)){
            waitMoment();
            count++;
            logger.info("waring kafka consumer message={},retry={}", JSON.toJSONString(message), count);
            try {
                consumer(message);
            }catch (Exception e){
                continue;
            }
            return true;
        }
        logger.error("kafka consumer error message={}", JSON.toJSONString(message));
        return false;

    }

    private void waitMoment() {
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException e) {
        }
    }
}
