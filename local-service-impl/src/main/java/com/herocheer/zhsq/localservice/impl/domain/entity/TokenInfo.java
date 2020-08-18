package com.herocheer.zhsq.localservice.impl.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TokenInfo {
    /**
     * 采集系统编号
     */
    String gatherSystemCode;
    /**
     * token
     */
    String tokenValue;

    String gatherSystemName;

    String password;

    Date effectiveTime;

    String domainName;

    String memo;

    Integer id;

    String type;

    String account;

    String manufacturer;

    Integer status;

    Integer port;


}
