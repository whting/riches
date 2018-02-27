package cn.jbricks.module.kafka.handle;

import cn.jbricks.module.kafka.model.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:57 2018/2/27
 */
public abstract class AbstractConsumerHandler<T> implements ConsumerHandler<T> {


    @Override
    public Message<T> parseByte(byte[] data) {
        String json = new String(data, Charset.forName("utf-8"));

        JSONObject jsonObject = JSON.parseObject(json);
        String msgId = jsonObject.getString("msgId");
        String key = jsonObject.getString("key");
        int reconsumeTimes = jsonObject.getInteger("reconsumeTimes");
        long startDeliverTime = jsonObject.getLong("startDeliverTime");
        JSONObject object = jsonObject.getJSONObject("model");
        T model = JSONObject.toJavaObject(object, getGenericType());

        Message<T> message = new Message(msgId,key,model,reconsumeTimes,startDeliverTime);
        return message;
    }

    public Class<T> getGenericType() {
        Class<T> clz = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return clz;
    }
}
