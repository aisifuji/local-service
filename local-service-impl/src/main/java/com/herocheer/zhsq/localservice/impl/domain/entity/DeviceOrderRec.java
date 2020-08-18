package com.herocheer.zhsq.localservice.impl.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
import lombok.Data;

/**
 * 设备指令接收信息表(DeviceOrderRec)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:30:18
 */
@Data
public class DeviceOrderRec extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -34071148896488018L;
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
    * 来自数据字典：10031
      10: 远程开门
    */
    private String orderCode;
    /**
    * 最新下发列表对应的id，用于数据获取后，本地更新成功后，能对应到对哪条下发发任务，更新反馈信息
    */
    private Integer newKeyid;



}