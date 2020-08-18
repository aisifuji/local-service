package com.herocheer.zhsq.localservice.impl.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 门禁开锁记录(GuardOpenRecordMsg)实体类
 *
 * @author makejava
 * @since 2020-06-29 10:57:36
 */
@Data
public class GuardOpenRecordMsg extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -34130992896876567L;
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
    * 人员Id
    */
    private Integer userId;
    /**
    * 人员姓名
    */
    private String userName;
    /**
    * 用于标注此人员是线上还是线下人员
0 : 线上
1：线上
    */
    private Object onlineFlg;
    /**
    * 手机号码
    */
    private String mobile;
    /**
    * 房屋号
    */
    private String address;
    /**
    * 来自数据字典10033

业主	10
家属	20
租客	30
                                                                   -&#&
    */
    private String userIdentity;
    /**
    * 来自数据字典 10037
00 进
10 出
    */
    private String inOutType;
    /**
    * 开锁时间
    */
    private Date catchTime;
    /**
    * 来自数据字典10032
10：刷脸开门
20：刷卡开门
    */
    private String openType;
    /**
    * 0：开锁失败
1；开锁成功
    */
    private Object openFlg;
    /**
    * 来自数据字典：10029
10：白名单
20：黑名单
    */
    private String userType;
    /**
    * 人脸现场照片url
    */
    private String facePicUrl;
    /**
    * 背景照片url
    */
    private String backgroundPicUrl;
    /**
    * 人脸比对值
    */
    private String faceMatchDegree;


    @TableField(exist = false)
    private String backgroundPic;
    @TableField(exist = false)
    private String facePic;


}