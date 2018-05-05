package cn.jbricks.module.kafka.consumer.work;

import cn.jbricks.module.kafka.consumer.SaveOffsetOnRebalance;
import cn.jbricks.module.kafka.consumer.handler.ConsumerHandler;
import cn.jbricks.module.kafka.consumer.thread.StandardThreadExecutor;
import cn.jbricks.module.kafka.message.KafkaMessage;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerWorker implements Runnable,DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(ConsumerWorker.class);

    private KafkaConsumer<String, Serializable> consumer;

    private String topic;

    private Properties props;

    private ConsumerHandler consumerHandler;

    private boolean offsetAutoCommit;

    private static final int maxProcessThreads = 200;

    private static final long pollTimeout = 1000;


    private StandardThreadExecutor defaultProcessExecutor;
    //高优先级处理线程池
    private StandardThreadExecutor highProcessExecutor;

    private volatile boolean isCommiting = false;

    //执行线程池满了被拒绝任务处理线程池
    private ExecutorService poolRejectedExecutor = Executors.newSingleThreadExecutor();


    private Map<TopicPartition, OffsetAndMetadata> uncommittedOffsetMap = new ConcurrentHashMap<>();

    private AtomicInteger uncommittedNums = new AtomicInteger(0); //当前未提交记录

    public ConsumerWorker(String topic, Properties props, ConsumerHandler consumerHandler, boolean offsetAutoCommit) {
        this.topic = topic;
        this.props = props;
        this.offsetAutoCommit = offsetAutoCommit;
        this.consumerHandler = consumerHandler;
        this.consumer = new KafkaConsumer(props);
        this.defaultProcessExecutor = new StandardThreadExecutor(1, maxProcessThreads,30, TimeUnit.SECONDS, maxProcessThreads,new StandardThreadExecutor.StandardThreadFactory("defaultProcessExecutor"),new PoolFullRunsPolicy());
        this.highProcessExecutor = new StandardThreadExecutor(1, 10,30, TimeUnit.SECONDS, maxProcessThreads,new StandardThreadExecutor.StandardThreadFactory("highProcessExecutor"),new PoolFullRunsPolicy());

        subscribeTopic();
    }

    private void subscribeTopic(){

        List<String> topics = new ArrayList<>(Arrays.asList(topic));

        if(offsetAutoCommit) {
            consumer.subscribe(topics);
        }else {
            ConsumerRebalanceListener listener = new SaveOffsetOnRebalance(consumer,uncommittedOffsetMap);
            consumer.subscribe(topics,listener);
        }
    }

    @Override
    public void run() {

        while (true){
            //当处理线程满后，阻塞处理线程
            while(true){
                if(defaultProcessExecutor.getMaximumPoolSize() > defaultProcessExecutor.getSubmittedTasksCount()){
                    break;
                }
                try {Thread.sleep(100);} catch (Exception e) {}
            }

            ConsumerRecords<String,Serializable> records = null;
            records = consumer.poll(pollTimeout);

            // no record found
            if (records.isEmpty()) {
                continue;
            }

            for (final ConsumerRecord<String,Serializable> record : records) {
                processConsumerRecords(record);
            }

            //提交分区
            if(uncommittedNums.get() > 0){
                commitOffsets();
            }

        }


    }

    /**
     * @param record
     */
    private void processConsumerRecords(final ConsumerRecord<String, Serializable> record) {
        //兼容没有包装的情况
        final KafkaMessage message = record.value() instanceof KafkaMessage ? (KafkaMessage) record.value() : new KafkaMessage(record.key(),(Serializable) record.value());

        message.setTopicMetadata(record.topic(), record.partition(), record.offset());

        //第一阶段处理
        consumerHandler.p1Process(message);
        //第二阶段处理
        (message.isConsumerAckRequired() ? highProcessExecutor : defaultProcessExecutor).submit(new Runnable() {
            @Override
            public void run() {
                try {
                    consumerHandler.p2Process(message);
                    //
                    if(!offsetAutoCommit){
                        uncommittedOffsetMap.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                        //
                        uncommittedNums.incrementAndGet();
                    }

                    //回执
                    if(message.isConsumerAckRequired()){
                        //TODO
                    }
                    //
                } catch (Exception e) {
                    consumerHandler.onProcessError(message);
                    logger.error("["+ consumerHandler.getClass().getSimpleName()+"] process Topic["+record.topic()+"] error",e);
                }
            }
        });
    }

    private void commitOffsets() {

        if(isCommiting)return;
        isCommiting = true;

        try {

            if(uncommittedOffsetMap.isEmpty())return ;

            logger.debug("committing the offsets : {}",uncommittedOffsetMap);
            consumer.commitAsync(uncommittedOffsetMap, new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    //
                    isCommiting = false;
                    if(exception == null){
                        resetUncommittedOffsetMap();
                        logger.debug("committed the offsets : {}",offsets);
                    }else{
                        logger.error("committ the offsets error",exception);
                    }
                }
            });
        } finally {
            isCommiting=false;
        }
    }

    private void resetUncommittedOffsetMap(){
        uncommittedOffsetMap.clear();
        uncommittedNums.set(0);
    }

    @Override
    public void destroy() throws Exception {
        consumer.close();
        poolRejectedExecutor.shutdown();
        defaultProcessExecutor.shutdown();
        highProcessExecutor.shutdown();

    }

    /**
     * 处理线程满后策略
     * @description <br>
     * @author <a href="mailto:vakinge@gmail.com">vakin</a>
     * @date 2016年7月25日
     */
    private class PoolFullRunsPolicy implements RejectedExecutionHandler {

        public PoolFullRunsPolicy() {}
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            poolRejectedExecutor.execute(r);
        }
    }
}