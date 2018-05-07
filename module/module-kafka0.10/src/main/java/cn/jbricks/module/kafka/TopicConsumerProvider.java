package cn.jbricks.module.kafka;

import cn.jbricks.module.kafka.consumer.config.ConsumerConfig;
import cn.jbricks.module.kafka.consumer.handler.ConsumerHandler;
import cn.jbricks.module.kafka.consumer.thread.StandardThreadExecutor;
import cn.jbricks.module.kafka.consumer.work.ConsumerWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午1:03 2018/5/5
 */
public class TopicConsumerProvider implements InitializingBean, DisposableBean {


    private ConsumerConfig config;


    private Map<String, ConsumerHandler> topicHandlers;

    private List<ConsumerWorker> consumerWorks = new ArrayList<>();

    private boolean offsetAutoCommit = true;

    private static StandardThreadExecutor fetchExecutor;


    @Override
    public void afterPropertiesSet() throws Exception {


        Validate.notNull(this.config, "configs is required");
        Validate.notEmpty(this.config.getServers(), "kafka configs[bootstrap.servers] is required");


        Properties props = new Properties();
        props.put("bootstrap.servers", config.getServers());
        props.put("enable.auto.commit", config.isAutoCommit());

        //make sure that rebalance.max.retries * rebalance.backoff.ms > zookeeper.session.timeout.ms.
        props.put("rebalance.max.retries", config.getRebalanceMaxRetries());
        props.put("rebalance.backoff.ms", config.getRebalanceBackoffMs());
        props.put("zookeeper.session.timeout.ms", config.getZookeeperSessionTimeoutMs());

        props.put("key.deserializer", config.getKeyDeserializer());
        props.put("value.deserializer", config.getValueDeserializer());
        props.put("auto.offset.reset", config.getAutoOffsetReset());
        props.put("group.id", config.getGroupId());


        offsetAutoCommit = config.isAutoCommit();

        int poolSize = topicHandlers.values().size();


        String topicPrefix = config.getTopicPrefix();
        if(StringUtils.isNotEmpty(topicPrefix)){
            Map<String, ConsumerHandler> newTopicHandlers = new HashMap<>();
            for(Map.Entry<String, ConsumerHandler> entry:topicHandlers.entrySet()){
                newTopicHandlers.put(warpTopic(entry.getKey()),entry.getValue());
            }
            topicHandlers = newTopicHandlers;
        }


        fetchExecutor = new StandardThreadExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, poolSize, new StandardThreadExecutor.StandardThreadFactory("KafkaFetcher"));
        ;


        List<String> topics = new ArrayList<>(topicHandlers.keySet());

        for (String topic : topics) {
            ConsumerWorker worker = new ConsumerWorker(topic, props, topicHandlers.get(topic), offsetAutoCommit);

            consumerWorks.add(worker);

            fetchExecutor.execute(worker);
        }

    }

    @Override
    public void destroy() throws Exception {
        fetchExecutor.shutdown();
    }

    public ConsumerConfig getConfig() {
        return config;
    }

    public void setConfig(ConsumerConfig config) {
        this.config = config;
    }

    public Map<String, ConsumerHandler> getTopicHandlers() {
        return topicHandlers;
    }

    public void setTopicHandlers(Map<String, ConsumerHandler> topicHandlers) {
        this.topicHandlers = topicHandlers;
    }


    private String warpTopic(String topic){
        if(StringUtils.isEmpty(config.getTopicPrefix())){
            return topic;
        }
        return config.getTopicPrefix()+"."+topic;
    }


}
