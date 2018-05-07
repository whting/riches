package cn.jbricks.module.kafka;

import cn.jbricks.module.kafka.producer.config.ProducerConfig;
import cn.jbricks.module.kafka.producer.handler.ProducerEventHandler;
import cn.jbricks.module.kafka.message.KafkaMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午6:05 2018/5/4
 */
public class TopicProducerProvider implements InitializingBean, DisposableBean {


    private static final Logger logger = LoggerFactory.getLogger(TopicProducerProvider.class);

    private ProducerConfig config;

    private List<ProducerEventHandler> eventHanlders = new ArrayList<>();

    private Producer producer;

    //默认是否异步发送
    private boolean defaultAsynSend = true;


    @Override
    public void afterPropertiesSet() throws Exception {
        Validate.notNull(this.config, "configs is required");

        Properties props = convert2Props(this.config);

        this.producer = new KafkaProducer<String, String>(props);
    }

    @Override
    public void destroy() throws Exception {
        producer.close();
    }


    private Properties convert2Props(ProducerConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getServers());
        props.put("acks", config.getAcks());
        props.put("retries", config.getRetries());
        props.put("batch.size", config.getBatchSize());
        props.put("key.serializer", config.getKeySerializer());
        props.put("value.serializer", config.getValueSerializer());
        defaultAsynSend = config.isAsync();
        return props;
    }


    public boolean publish(String topicName, final KafkaMessage message) {
        topicName = warpTopic(topicName);
        return publish(topicName,message,defaultAsynSend);
    }

    public boolean publish(final String topicName, final KafkaMessage message, boolean asynSend) {
        Validate.notNull(topicName, "Topic is required");

        Validate.notNull(message, "KafkaMessage is required");

        //异步 ，如果需要回执强制同步发送
        if(asynSend){
            doAsynSend(topicName, message.getMsgId(),message);
        }else{
            doSyncSend(topicName, message.getMsgId(), message);
        }

        return true;
    }

    private void doSyncSend(String topicName, String messageKey, KafkaMessage message) {
        ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(topicName, messageKey, message);
        try {
            Future<RecordMetadata> future = this.producer.send(record);

            RecordMetadata metadata = future.get();
            for (ProducerEventHandler handler : eventHanlders) {
                try {handler.onSuccessed(topicName, metadata);} catch (Exception e) {}
            }
            if (logger.isDebugEnabled()) {
                logger.debug("kafka_send_success,topic=" + topicName + ", messageId=" + messageKey + ", partition=" + metadata.partition() + ", offset=" + metadata.offset());
            }
        }catch (Exception ex){
            for (ProducerEventHandler handler : eventHanlders) {
                try {handler.onError(topicName, message);} catch (Exception e) {}
            }
            logger.error("kafka_send_fail,topic="+topicName+",messageId="+messageKey,ex);
            //同步发送直接抛异常
            throw new RuntimeException(ex);
        }

    }

    private void doAsynSend(final String topicName, final String messageKey, final KafkaMessage message) {
        ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(topicName, messageKey, message);

        this.producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception ex) {
                if (ex != null) {
                    for (ProducerEventHandler handler : eventHanlders) {
                        try {handler.onError(topicName, message);} catch (Exception e) {}
                    }
                    logger.error("kafka_send_fail,topic="+topicName+",messageId="+messageKey,ex);
                } else {
                    for (ProducerEventHandler handler : eventHanlders) {
                        try {handler.onSuccessed(topicName, metadata);} catch (Exception e) {}
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("kafka_send_success,topic=" + topicName + ", messageId=" + messageKey + ", partition=" + metadata.partition() + ", offset=" + metadata.offset());
                    }
                }
            }
        });
    }

    public boolean publishNoWrapperMessage(String topicName, Serializable message) {
        return publishNoWrapperMessage(topicName, null, message, defaultAsynSend);
    }

    public boolean publishNoWrapperMessage(String topicName, Serializable message, boolean asynSend) {
        return publishNoWrapperMessage(topicName, null, message, asynSend);
    }

    public boolean publishNoWrapperMessage(String topicName, String msgId, Serializable message, boolean asynSend) {
        KafkaMessage Message = new KafkaMessage(msgId,message);
        topicName = warpTopic(topicName);

        return publish(topicName, Message,asynSend);
    }

    private String warpTopic(String topic){
        if(StringUtils.isEmpty(config.getTopicPrefix())){
            return topic;
        }
        return config.getTopicPrefix()+"."+topic;
    }


    public ProducerConfig getConfig() {
        return config;
    }

    public void setConfig(ProducerConfig config) {
        this.config = config;
    }

    public List<ProducerEventHandler> getEventHanlders() {
        return eventHanlders;
    }

    public void setEventHanlders(List<ProducerEventHandler> eventHanlders) {
        this.eventHanlders = eventHanlders;
    }
}
