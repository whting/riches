package cn.jbricks.module.kafka.serializer;

import cn.jbricks.module.kafka.message.KafkaMessage;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年3月11日
 */
public class JsonMessageDeserializer implements Deserializer<Object> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Object deserialize(String topic, byte[] data) {
    	try {
            if (data == null)
                return null;
            else{            	
            	String jsonString = new String(data, StandardCharsets.UTF_8.name());
            	try {
            		return JSON.parseObject(jsonString, KafkaMessage.class);
				} catch (Exception e) {
					return jsonString;
				}
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding UTF-8");
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}
