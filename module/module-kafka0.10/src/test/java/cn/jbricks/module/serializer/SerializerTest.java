package cn.jbricks.module.serializer;

import cn.jbricks.module.kafka.message.KafkaMessage;
import cn.jbricks.module.kafka.serializer.JsonMessageDeserializer;
import cn.jbricks.module.kafka.serializer.JsonMessageSerializer;
import org.junit.Test;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * @Author: haoting.wang
 * @Date: Created in 上午11:50 2018/5/5
 */
public class SerializerTest {

    @Test
    public void testMessagePack() throws IOException {

        MessagePack pack = new MessagePack();
        KafkaMessage message = new KafkaMessage("1","hello world");

        byte[] write = pack.write(message);

        KafkaMessage read = pack.read(write,KafkaMessage.class);

        System.out.println(read.getBody());

        //报错，因为MessagePack 不支持泛型
    }


    @Test
    public void testJsonSerializer() throws IOException {
        JsonMessageSerializer serializer = new JsonMessageSerializer();

        KafkaMessage message = new KafkaMessage("1","hello world");
        byte[] datas = serializer.serialize("test", message);

        JsonMessageDeserializer deserializer = new JsonMessageDeserializer();

        KafkaMessage result = (KafkaMessage)deserializer.deserialize("test", datas);

        System.out.println(result.getBody());


    }
}
