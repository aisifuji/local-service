package com.herocheer.zhsq.localservice.core.device.entity;

import lombok.Data;

@Data
public class DeviceResponse {
    private Boolean isSuccess;
    private String message;
    public DeviceResponse(Boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public DeviceResponse(Boolean isSuccess){
        this(isSuccess,null);
    }

}
