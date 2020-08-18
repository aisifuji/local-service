package com.herocheer.zhsq.localservice.core.device.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BehaviorAnalysisData   {
    /**
     * id
     */
    private Integer id;
    /**
     * 预警时间
     */
    private Date alarmTime;
    /**
     * 直接存储事件名称，不存储事件代码
     */
    private String eventName;
    /**
     * 设备ID
     */
    private String deviceSn;
    /**
     * 视频url地址
     */
    private String videoUrl;
    /**
     * 抓拍照片url地址
     */
    private String picUrl;
    /**
     * 地点
     */
    private String address;
}
