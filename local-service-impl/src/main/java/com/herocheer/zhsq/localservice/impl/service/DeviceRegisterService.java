package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;

import java.util.List;

public interface DeviceRegisterService {


    void saveOrUpdateDeviceRegister(DeviceRegister deviceRegister);

    DeviceRegister queryOneByDeviceSn(String deviceSn);

    List<DeviceRegister> queryList();

    void deleteDeviceRegisterByDeviceSn(DeviceRegister deviceRegister);

}
