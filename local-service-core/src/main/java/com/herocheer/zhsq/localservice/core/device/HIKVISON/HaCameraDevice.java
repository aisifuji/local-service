package com.herocheer.zhsq.localservice.core.device.HIKVISON;

import com.herocheer.zhsq.localservice.core.device.AbstractDevice;
import com.herocheer.zhsq.localservice.core.device.Device;
import com.herocheer.zhsq.localservice.core.device.entity.*;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HaCameraDevice extends AbstractDevice {


    @Autowired
    private HIKSdkHandler hikSdkHandler;
    //设备类型
    private static final Integer deviceType = 20; //摄像头
    //设备厂商标志
    private static final Integer brand = 10; //海康威视
    //设备支持功能
    private static final Integer deviceSupFun1 = 10; //摄像头下是普通IPC

    private static final Logger logger = LoggerFactory.getLogger(HaCameraDevice.class);

    public HaCameraDevice() {
        super(deviceType,brand, Arrays.asList(deviceSupFun1));
    }

//    @Override
//    public DeviceResponse registerDevice(BaseDevice baseDevice) {
//        Assert.notNull(baseDevice,"device is null");
//        int deviceId = hikSdkHandler.getHcNetSDK().NET_DVR_Login_V30(baseDevice.getIp(), baseDevice.getPort().shortValue(), baseDevice.getAccount(), baseDevice.getPassword(), null);
//        if(deviceId!=-1){
//            logger.info("设备id:{}",deviceId);
//            //登出
//            hikSdkHandler.getHcNetSDK().NET_DVR_Logout_V30(deviceId);
//            return new DeviceResponse(true,"注册成功");
//        }
//        return new DeviceResponse(false,hikSdkHandler.getHcNetSDK().NET_DVR_GetErrorMsg(new IntByReference(hikSdkHandler.getHcNetSDK().NET_DVR_GetLastError())));
//    }

    @Override
    public DeviceResponse doRegisterDevice(BaseDevice baseDevice) {
        Assert.notNull(baseDevice,"device is null");
        int deviceId = hikSdkHandler.getHcNetSDK().NET_DVR_Login_V30(baseDevice.getIp(), baseDevice.getPort().shortValue(), baseDevice.getAccount(), baseDevice.getPassword(), null);
        if(deviceId!=-1){
            logger.info("设备id:{}",deviceId);
            //登出
            hikSdkHandler.getHcNetSDK().NET_DVR_Logout_V30(deviceId);
            return new DeviceResponse(true,"注册成功");
        }
        return new DeviceResponse(false,hikSdkHandler.getHcNetSDK().NET_DVR_GetErrorMsg(new IntByReference(hikSdkHandler.getHcNetSDK().NET_DVR_GetLastError())));
    }

    @Override
    public DeviceResponse setFace(BaseDevice baseDevice, BaseFace baseFace) {
       if(!isSupportSetFace()){
           throw new CheckedException("device-0005");
       }
       return null;
    }

    @Override
    public DeviceResponse updateFace(BaseDevice baseDevice, BaseFace baseFace) {
        if(!isSupportSetFace()){
            throw new CheckedException("device-0005");
        }
        return null;
    }

    @Override
    public DeviceResponse delFace(BaseDevice baseDevice, BaseFace baseFace) {
        if(!isSupportSetFace()){
            throw new CheckedException("device-0005");
        }
        return null;
    }

    @Override
    public DeviceResponse setCard(BaseDevice baseDevice, BaseCard baseCard) {
        if(!isSupportSetFace()){
            throw new CheckedException("device-0006");
        }
        return null;
    }

    @Override
    public DeviceResponse updateCard(BaseDevice baseDevice, BaseCard baseCard) {
        if(!isSupportSetFace()){
            throw new CheckedException("device-0006");
        }
        return null;
    }

    @Override
    public DeviceResponse delCard(BaseDevice baseDevice, BaseCard baseCard) {
        if(!isSupportSetFace()){
            throw new CheckedException("device-0006");
        }
        return null;
    }

//    @Override
//    public Integer getDeviceType() {
//        return this.deviceType;
//
//    }

//    @Override
//    public List<Integer> getDeviceSupFun() {
//        List<Integer> typeList = new ArrayList<>();
//        typeList.add(deviceSupFun1);
//        return typeList;
//    }
    @Override
    public Boolean isSupportSetFace() {
//       throw new CheckedException("device-0005");
       return false;
    }

    @Override
    public Boolean isSupportSetCard() {
//        throw new CheckedException("device-0006");
        return false;
    }

    @Override
    public Boolean isSupportCapture() {
//        throw new CheckedException("device-0009");
        return false;
    }

    @Override
    public void captureEvent(BaseDevice baseDevice) {
    }

    @Override
    public DeviceResponse isOnline(BaseDevice baseDevice) {
        DeviceResponse deviceResponse = this.registerDevice(baseDevice);
        logger.info("设备序列号：{}，在线情况：{}",baseDevice.getDeviceSn(), deviceResponse.getIsSuccess()?"在线":"离线");
        return deviceResponse;

    }

//    @Override
//    public Integer getBrand() {
//        return this.brand;
//    }

    @Override
    public DeviceResponse openGuardOrder(BaseDevice baseDevice) {
        return null;
    }

}
