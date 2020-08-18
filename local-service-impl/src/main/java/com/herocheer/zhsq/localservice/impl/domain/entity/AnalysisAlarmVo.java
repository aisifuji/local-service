package com.herocheer.zhsq.localservice.impl.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class AnalysisAlarmVo implements Serializable {
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
    private String eventContent;
    /**
     * 视频url地址
     */
    private String video;
    /**
     * 抓拍照片url地址
     */
    private String pic;
    /**
     * 地点
     */
    private String address;
}
