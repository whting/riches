package cn.jbricks.module.kafka.model;

import java.io.Serializable;

/**
 * Created by haoting.wang on 17/2/28.
 */
public class Message<T> implements Serializable {

    private String msgId;
    private String key;             // 消息对应的业务实体的id
    private T      model;

    private int    reconsumeTimes;
    private long   startDeliverTime;

    public Message(){
    }

    public Message(String msgId, String key, T model, int reconsumeTimes, long startDeliverTime) {
        this.msgId = msgId;
        this.key = key;
        this.model = model;
        this.reconsumeTimes = reconsumeTimes;
        this.startDeliverTime = startDeliverTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getReconsumeTimes() {
        return reconsumeTimes;
    }

    public void setReconsumeTimes(int reconsumeTimes) {
        this.reconsumeTimes = reconsumeTimes;
    }

    public long getStartDeliverTime() {
        return startDeliverTime;
    }

    public void setStartDeliverTime(long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }
}
