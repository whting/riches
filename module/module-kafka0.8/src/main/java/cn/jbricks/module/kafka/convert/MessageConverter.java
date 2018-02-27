package cn.jbricks.module.kafka.convert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午2:57 2018/2/27
 */
public interface MessageConverter {
    byte[] toByte(Object object) throws UnsupportedEncodingException;

    Object toObject(byte[] body, Class clazz) throws IOException;
}
