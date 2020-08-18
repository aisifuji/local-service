package com.herocheer.zhsq.localservice.impl.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
/**
 * 门禁卡号信息表(GuardCardMsg)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:43:35
 */
@Data
public class GuardCardMsg extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -35686614155271289L;
    /**
    * id
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 来自数据字典：10033
10 业主
20 家属
30 租客
40 租客
    */
    private String liveIdentity;
    /**
    * 设备ID
    */
    private Integer deviceId;
    /**
    * 设备序列号
    */
    private String deviceSn;
    /**
    * 门禁卡号
    */
    private String cardNo;
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
    * 来自数据字典10028
10：新增
20：更新
30：删除
    */
    private String opType;
    /**
    * 有效开始时间
    */
    private Date effStartTime;
    /**
    * 有效结束时间
    */
    private Date effEndTime;
    /**
    * 限制次数
    */
    private Integer limitNumber;
    /**
    * 房屋号
    */
    private String address;
    /**
    * 最新下发列表对应的id，用于数据获取后，本地更新成功后，能对应到对哪条下发发任务，更新反馈信息
    */
    private Integer newKeyid;



}