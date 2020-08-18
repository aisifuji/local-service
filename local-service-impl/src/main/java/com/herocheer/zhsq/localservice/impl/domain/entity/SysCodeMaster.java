package com.herocheer.zhsq.localservice.impl.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.herocheer.zhsq.localservice.core.base.BaseEntity;
/**
 * 主要是指令，更新频率低(SysCodeMaster)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:44:50
 */
@Data
public class SysCodeMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -99936157706199427L;
    /**
    * id
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 字典名称
    */
    private String dictName;
    /**
    * 字典值
    */
    private String dictValue;
    /**
    * 字典主表编码
    */
    private String groupCode;



}