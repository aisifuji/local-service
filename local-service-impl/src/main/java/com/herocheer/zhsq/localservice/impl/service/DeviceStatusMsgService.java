package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceStatusMsg;

public interface DeviceStatusMsgService {


    DeviceStatusMsg queryOneByDeviceSn(String deviceSn);

    void saveOrUpdateDeviceStatusMsg(DeviceStatusMsg deviceStatusMsg);



}
