package com.herocheer.zhsq.localservice.impl.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 行为分析预警表(BehaviorAnalysisAlarm)实体类
 *
 * @author makejava
 * @since 2020-07-08 09:52:00
 */
@Data
public class BehaviorAnalysisAlarm implements Serializable {
    /**
    * id
    */
    @TableId(value = "id",type = IdType.AUTO)
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
    /**
    * create_time
    */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
    * modify_time
    */
    @TableField(fill = FieldFill.UPDATE)
    private Date modifyTime;
}