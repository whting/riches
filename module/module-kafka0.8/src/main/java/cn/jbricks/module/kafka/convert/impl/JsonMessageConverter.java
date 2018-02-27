package cn.jbricks.module.kafka.convert.impl;

import cn.jbricks.module.kafka.convert.MessageConverter;
import cn.jbricks.module.kafka.model.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:57 2018/2/27
 */
public class JsonMessageConverter implements MessageConverter {
    public static final Logger logger = LoggerFactory.getLogger(JsonMessageConverter.class);

    @Override
    public byte[] toByte(Object object) throws UnsupportedEncodingException {

        String jsonData = JSON.toJSONString(object);

        return jsonData.getBytes("utf-8");
    }

    @Override
    public Message toObject(byte[] body, Class clazz) throws IOException {

        String json = new String(body, Charset.forName("utf-8"));

        JSONObject jsonObject = JSON.parseObject(json);
        String msgId = jsonObject.getString("msgId");
        String key = jsonObject.getString("key");
        int reconsumeTimes = jsonObject.getInteger("reconsumeTimes");
        long startDeliverTime = jsonObject.getLong("startDeliverTime");
        JSONObject object = jsonObject.getJSONObject("model");
        Object model = JSONObject.toJavaObject(object, clazz);

        Message message = new Message(msgId,key,model,reconsumeTimes,startDeliverTime);
        return message;
    }
}
