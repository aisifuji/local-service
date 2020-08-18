package com.herocheer.zhsq.localservice.core.device;

import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractDevice implements Device {

    private Integer deviceType; //设备类型

    private Integer brand; //设备厂商sdk相关

    private List<Integer> deviceSupFun; //设备支持功能

    private static final Logger logger = LoggerFactory.getLogger(AbstractDevice.class);

    protected AbstractDevice(Integer deviceType, Integer brand, List<Integer> deviceSupFun){
        logger.info("启动设备对应实例，设备类型：{},设备厂商：{},设备支持功能：{}",deviceType,brand,deviceSupFun);
        this.deviceType = deviceType;
        this.brand = brand;
        this.deviceSupFun = deviceSupFun;
    }

    @Override
    public DeviceResponse registerDevice(BaseDevice baseDevice) {
        DeviceResponse deviceResponse = doRegisterDevice(baseDevice);
        //注册成功
        if(deviceResponse.getIsSuccess()){
            //启动抓拍
            if(isSupportCapture()){
                captureEvent(baseDevice);
            }
        }
        return deviceResponse;
    }

    @Override
    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    @Override
    public List<Integer> getDeviceSupFun() {
        return deviceSupFun;
    }

    public void setDeviceSupFun(List<Integer> deviceSupFun) {
        this.deviceSupFun = deviceSupFun;
    }

    public abstract DeviceResponse doRegisterDevice(BaseDevice baseDevice);
}
