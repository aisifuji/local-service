package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;

public interface DeviceUploadService {

    void uploadRecEvent(Integer id, String topic, DeviceResponse deviceResponse);

    void uploadDeviceStatus(DeviceRegister deviceRegister);
}
