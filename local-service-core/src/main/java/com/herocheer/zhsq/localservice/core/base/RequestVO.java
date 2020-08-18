package com.herocheer.zhsq.localservice.core.base;

import lombok.Data;

import java.util.List;

/**
 * @Author: wuyizhou
 * @Description:
 * @Version: 1.0
 */
@Data
public class RequestVO {

    /**
     * 加密的密文
     */
    private String data;

    /**
     * 上传图片 base64位 批量
     */
    private List<String> imgs;
    /**
     * 上传图片 base64位 单张
     */
    private  String  img;
}
