package com.herocheer.zhsq.localservice.impl.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
/**
 * 设备在线状态监测表(DeviceStatusMsg)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:41:30
 */
@Data
public class DeviceStatusMsg extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 891252282303024463L;
    /**
    * id
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 设备序列号
    */
    private String deviceSn;
    /**
    * 设备心跳
    */
    private Integer deviceBeat;
    /**
    * 0：离线
1：在线
    */
    private Integer status;
    /**
    * 最后检测时间
    */
    private Date lastCheckTime;
    /**
    * 心跳间隔
    */
    private Integer checkInterval;



}