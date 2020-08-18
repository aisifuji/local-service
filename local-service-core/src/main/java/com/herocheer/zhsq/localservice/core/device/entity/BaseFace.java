package com.herocheer.zhsq.localservice.core.device.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseFace {
    private String userId;

    private String name;

    private Integer role;

    private String jpgFilePath;

    private Date effStartTime;

    private Date effEndTime;


    public BaseFace(String userId, String name, Integer role, String jpgFilePath, Date effStartTime, Date effEndTime) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.jpgFilePath = jpgFilePath;
        this.effStartTime = effStartTime;
        this.effEndTime = effEndTime;
    }
}
