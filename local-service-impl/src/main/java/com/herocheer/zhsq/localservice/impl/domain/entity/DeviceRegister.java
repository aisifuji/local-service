package com.herocheer.zhsq.localservice.impl.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
/**
 * 设备注册表(DeviceRegister)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:40:26
 */
@Data
public class DeviceRegister extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 351280433353782195L;
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
    * 设备名称
    */
    private String deviceNm;
    /**
    * 数据字典编号：10026
10：门禁机
20：摄像头
    */
    private String deviceType;
    /**
    * 根据设备类型，会有不同的设备支持功能类型
门禁设备下支持功能类型有：
10：人脸
2
    */
    private String deviceSupFun;
    /**
    * 登录IP
    */
    private String loginIp;
    /**
    * 登录账号
    */
    private String account;
    /**
    * 登录密码
    */
    private String password;
    /**
    * 数据来自数据字典10027
10：海康威视
    */
    private String brand;
    /**
    * 品牌型号
    */
    private String brandModel;
    /**
    * 设备软件版本
    */
    private String softwareVersion;
    /**
    * 设备国标ID
    */
    private String deviceNaStandard;
    /**
    * 来自数据字典10028
10：新增
20：更新
30：删除
    */
    private String opType;
    /**
    * 最新下发列表对应的id，用于数据获取后，本地更新成功后，能对应到对哪条下发发任务，更新反馈信息
    */
    private Integer newKeyid;
    /**
     * 设备心跳
     */
    @TableField(exist = false)
   private Integer online;

    private Integer port;
//    /**
//     * 兼容海康deviceId
//     */
//    @TableField(exist = false)
//    private String deviceId;
    /**
     * 启用标志
     */
    private Integer deviceStatus;

}