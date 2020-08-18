package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;
import com.herocheer.zhsq.localservice.impl.service.DeviceUploadService;
import com.herocheer.zhsq.localservice.impl.support.CustomRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("deviceUploadService")
public class DeviceUploadServiceImpl implements DeviceUploadService {

    @Autowired
    private CustomRestTemplate customRestTemplate;
    @Autowired
    private CommonProperties commonProperties;
    @Override
    public void uploadRecEvent(Integer id, String topic, DeviceResponse deviceResponse) {
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        params.put("type",topic);
        params.put("isSuccess", deviceResponse.getIsSuccess());
        params.put("msg", deviceResponse.getMessage());
        customRestTemplate.execute(commonProperties.getRecStatusBackApi(), HttpMethod.POST, params, String.class);
    }

    @Override
    public void uploadDeviceStatus(DeviceRegister deviceRegister) {
        //上报心跳
        customRestTemplate.execute(commonProperties.getUploadDeviceStatusApi(), HttpMethod.POST, deviceRegister, String.class);
    }
}
