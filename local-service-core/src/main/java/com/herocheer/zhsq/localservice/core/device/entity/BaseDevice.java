package com.herocheer.zhsq.localservice.core.device.entity;


import lombok.Data;

@Data
public class BaseDevice {

    private String id;
    private String deviceSn;
    private String ip;
    private String account;
    private String password;
    private Integer port;

    public BaseDevice(String deviceSn,String ip,String account,String password,Integer port){
       this(null, deviceSn, ip, account, password, port);
    }
    public BaseDevice(String id,String deviceSn,String ip,String account,String password,Integer port){
        this.id = id;
        this.deviceSn = deviceSn;
        this.ip = ip;
        this.account = account;
        this.password = password;
        this.port = port;
    }

}
