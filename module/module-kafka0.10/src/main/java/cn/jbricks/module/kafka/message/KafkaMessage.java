package cn.jbricks.module.kafka.message;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午6:22 2018/5/4
 */
@Message
public class KafkaMessage implements Serializable {

    private String msgId;

    private Serializable body;

    private boolean consumerAckRequired = false;//是否需要消费回执

    private transient String topic;
    private transient int partition;
    private transient long offset;

    public KafkaMessage() {
    }

    public KafkaMessage(String msgId, Serializable body) {
        this.msgId = msgId;
        this.body = body;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Serializable getBody() {
        return body;
    }

    public void setBody(Serializable body) {
        this.body = body;
    }

    public boolean isConsumerAckRequired() {
        return consumerAckRequired;
    }

    public void setConsumerAckRequired(boolean consumerAckRequired) {
        this.consumerAckRequired = consumerAckRequired;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setTopicMetadata(String topic, int partition, long offset) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }
}
