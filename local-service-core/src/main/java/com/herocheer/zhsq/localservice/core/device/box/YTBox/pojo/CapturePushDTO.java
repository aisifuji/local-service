package com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author wind
 * @date 2019/10/23 18:18
 */
@Data
@ToString
public class CapturePushDTO {
    private String serial;
    private Integer channel;
    private Long timestamp;
    private String panorama_image;
    private String user;
    private String user_info;
    private String pg_id;
    private Integer ptype;
    private Float match;
    private Integer sex;
    private Integer age;
    private String user_image;
    private String face_image;
    private String mp4_path;
    private Integer left_top_x;
    private Integer left_top_y;
    private Integer right_bottom_x;
    private Integer right_bottom_y;
    private String age_range;
    private Integer beard;
    private Integer glasses;
    private Integer sun_glasses;
    private Integer mask;
    private Integer calling;
    private Integer hat;
    private String hair_style;
    private Integer smiley_face;
    private Integer skin_color;
    private Integer beard_style;
    private Integer hat_color;
}
