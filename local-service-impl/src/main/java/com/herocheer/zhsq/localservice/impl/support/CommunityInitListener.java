package com.herocheer.zhsq.localservice.impl.support;

import com.herocheer.zhsq.localservice.impl.domain.constant.CommunityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CommunityInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CustomRestTemplate customRestTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CommunityInitListener.class);


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        CommunityInfo.gatherSystemCode = customRestTemplate.getToken().getGatherSystemCode();
        logger.info("获取社区编号：{}",CommunityInfo.gatherSystemCode);
    }
}
