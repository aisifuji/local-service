package com.herocheer.zhsq.localservice.core.device.box.YTBox;

import lombok.Data;

@Data
public class YTBoxFace {

    private String id;

    private String address;

    private String sex;

    private String name;

    public YTBoxFace(String id, String address, String sex, String name) {
        this.id = id;
        this.address = address;
        this.sex = sex;
        this.name = name;
    }
}
