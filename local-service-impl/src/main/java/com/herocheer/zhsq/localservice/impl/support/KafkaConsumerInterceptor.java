package com.herocheer.zhsq.localservice.impl.support;

import com.herocheer.zhsq.localservice.core.util.JSDesUtils;
import com.herocheer.zhsq.localservice.impl.domain.constant.CommunityInfo;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KafkaConsumerInterceptor implements ConsumerInterceptor<String,String> {
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
        Map<TopicPartition, List<ConsumerRecord<String, String>>> newRecords = new HashMap<>();
        for(TopicPartition partition:consumerRecords.partitions()){
            List<ConsumerRecord<String, String>> recs = consumerRecords.records(partition);
            List<ConsumerRecord<String, String>> newRecs = new ArrayList<>();
            for(ConsumerRecord<String,String> rec:recs){
                //非本社区数据
                if(!rec.key().equals(CommunityInfo.gatherSystemCode)){
                    continue;
                }
                //解密
                try {
                    ConsumerRecord<String,String> newRec = new ConsumerRecord<>(rec.topic(),
                            rec.partition(),rec.offset(),rec.key(), JSDesUtils.fileDecode(rec.value(), 1));
                    newRecs.add(newRec);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            newRecords.put(partition,newRecs);
        }
        return new ConsumerRecords<>(newRecords);
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
