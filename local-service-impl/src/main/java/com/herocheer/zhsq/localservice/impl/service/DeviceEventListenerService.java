package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.core.device.entity.BaseEvent;
import org.springframework.context.ApplicationListener;

public interface DeviceEventListenerService extends ApplicationListener<BaseEvent> {
}
