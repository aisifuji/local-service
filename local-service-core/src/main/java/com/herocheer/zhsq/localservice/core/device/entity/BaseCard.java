package com.herocheer.zhsq.localservice.core.device.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseCard {

    private String userId;

    private String name;

    private String cardNo;

    private Integer role;

    private Date effStartTime;

    private Date effEndTime;

    public BaseCard(String userId, String name, String cardNo, Integer role, Date effStartTime, Date effEndTime) {
        this.userId = userId;
        this.name = name;
        this.cardNo = cardNo;
        this.role = role;
        this.effStartTime = effStartTime;
        this.effEndTime = effEndTime;
    }
}
