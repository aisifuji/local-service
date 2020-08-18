package com.herocheer.zhsq.localservice.impl.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员抓拍记录表(PersonPhotoFaceRecord)实体类
 *
 * @author makejava
 * @since 2020-06-29 10:56:58
 */
@Data
public class PersonPhotoFaceRecord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 738008081404076573L;
    /**
    * id
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 人员姓名
    */
    private String userName;
    /**
    * 体现在线上及线下人员user_id
    */
    private Integer userId;
    /**
    * 人脸照片url
    */
    private String facePicUrl;
    /**
    * 住户：业主、家属、租客
非住户：游客、陌生人
    */
    private String userIdentity;
    /**
    * 时间
    */
    private Date catchTime;
    /**
    * 现场背景照片
    */
    private String backgroundPicUrl;
    /**
    * 来自数据字典：10029
10：白名单
20：黑名单
    */
    private String userType;
    /**
    * 设备序列号
    */
    private String deviceSn;
    /**
    * 人脸匹配度
    */
    private String faceMatchDegree;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 年龄段
     */
    private String ageRange;

    @TableField(exist = false)
    private String backgroundPic;
    @TableField(exist = false)
    private String facePic;
    @TableField(exist = false)
    private String accessFaceUrl;
    @TableField(exist = false)
    private String communityId;
    @TableField(exist = false)
    private String dataSource;
    /**
     * 人员标签
     */
    private String personTagGroup;
    /**
     * 人员类型
     */
    @TableField(exist = false)
    private String personType;
    @TableField(exist = false)
    private String communityCode;

}