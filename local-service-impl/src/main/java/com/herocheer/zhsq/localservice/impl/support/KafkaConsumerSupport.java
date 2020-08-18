package com.herocheer.zhsq.localservice.impl.support;

import com.alibaba.fastjson.JSON;
import com.herocheer.zhsq.localservice.core.util.JSDesUtils;
import com.herocheer.zhsq.localservice.impl.domain.constant.AESKey;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


public class KafkaConsumerSupport {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerSupport.class);

    public static <T> T  transformMessage(String key, ConsumerRecord<?, ?> record, Class<?> clazz){
        Assert.hasText(key,"key is null");
        Assert.notNull(record,"record is null");
        Assert.notNull(clazz,"clazz is null");
        logger.info("开始消费kafka数据,社区编号为:{}",record.key());
        //判断key是否是本社区
        if(!key.equals(record.key())){
            return null;
        }
        //解密
        String decodeStr = JSDesUtils.fileDecode(String.valueOf(record.value()), AESKey.fileDeCodeSecretKey);
        return (T)JSON.parseObject(decodeStr, clazz);

    }

}
