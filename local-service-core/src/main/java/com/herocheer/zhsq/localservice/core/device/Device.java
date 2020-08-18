package com.herocheer.zhsq.localservice.core.device;

import com.herocheer.zhsq.localservice.core.device.entity.*;

import java.util.List;

public interface Device {

    /**
     * 设备注册
     * @param baseDevice
     * @return
     */
    DeviceResponse registerDevice(BaseDevice baseDevice);
    /**
     * 设置设备人脸
     * @param baseFace
     * @return 成功或者失败
     */
    DeviceResponse setFace(BaseDevice baseDevice, BaseFace baseFace);

    /**
     * 修改人脸信息
     * @param baseDevice
     * @param baseFace
     * @return
     */
    DeviceResponse updateFace(BaseDevice baseDevice, BaseFace baseFace);

    /**
     * 删除人脸信息
     * @param baseDevice
     * @param baseFace
     * @return
     */
    DeviceResponse delFace(BaseDevice baseDevice, BaseFace baseFace);

    /**
     * 设置设备卡信息
     * @param baseCard
     * @return 成功或者失败
     */
    DeviceResponse setCard(BaseDevice baseDevice, BaseCard baseCard);

    /**
     * 修改设备卡信息
     * @param baseCard
     * @return 成功或者失败
     */
    DeviceResponse updateCard(BaseDevice baseDevice, BaseCard baseCard);

    /**
     * 删除设备卡信息
     * @param baseCard
     * @return 成功或者失败
     */
    DeviceResponse delCard(BaseDevice baseDevice, BaseCard baseCard);



    /**
     * 获取设备类型（10门禁、20摄像）
     * @return 设备类型
     */
    Integer getDeviceType();

    /**
     * 获取设备类型（刷脸、刷卡、测温）
     * @return 设备类型
     */
    List<Integer> getDeviceSupFun();

    /**
     * 是否支持设置人脸
     * @return
     */
    Boolean isSupportSetFace();

    /**
     * 是否支持设置卡片
     * @return
     */
    Boolean isSupportSetCard();

    /**
     * 是否支持上报
     * @return
     */
    Boolean isSupportCapture();

    /**
     * 事件回调
     */
    void captureEvent(BaseDevice baseDevice);

    /**
     * 设备是否在线
     * @return
     */
    DeviceResponse isOnline(BaseDevice baseDevice);

    /**
     * 获取设备厂商sdk类型
     * @return
     */
    Integer getBrand();

    DeviceResponse openGuardOrder(BaseDevice baseDevice);

}
