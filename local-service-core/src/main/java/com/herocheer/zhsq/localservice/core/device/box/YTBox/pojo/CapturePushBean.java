package com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo;

import lombok.Data;

/**
 * @author wind
 * @date 2019/11/21 15:14
 */
@Data
public class CapturePushBean {
    private String randomStr;
    private String channelName;
    private String repositoryId;
    private String userId;
    private String userName;
    private Integer userType;
    private Long similarity;
    private Integer minAge;
    private Integer maxAge;
    private Integer sex;
    private Long timestamp;
    private String faceImage;
    private String sceneImage;
}
