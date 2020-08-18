package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.core.device.Device;
import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceOrderRec;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;
import com.herocheer.zhsq.localservice.impl.domain.entity.FaceMsg;
import com.herocheer.zhsq.localservice.impl.domain.entity.GuardCardMsg;
import org.springframework.boot.ApplicationRunner;

import java.text.ParseException;

public interface DeviceOperationService extends ApplicationRunner {

    DeviceResponse registerDevice(DeviceRegister deviceRegister);

    void getDeviceStatus(DeviceRegister deviceRegister);

    DeviceResponse setFace(DeviceRegister deviceRegister, FaceMsg faceMsg) throws ParseException;

    DeviceResponse sendOrder(DeviceOrderRec deviceOrderRec);

    DeviceResponse setCard(DeviceRegister deviceRegister, GuardCardMsg guardCardMsg) throws ParseException;

     Device getDeviceInstance(DeviceRegister deviceRegister);

}
