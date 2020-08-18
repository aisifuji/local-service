package com.herocheer.zhsq.localservice.core.device.box.YTBox.dict;

/**
 * @author wind
 * @date 2020/1/3 15:38
 */
public enum TypeEnum {
    KID("1-15岁"),
    KID_YOUTH("15-20岁"),
    YOUTH("20-30岁"),
    YOUTH_ADULT("30-35岁"),
    ADULT("35-45岁"),
    ADULT_OLD("45-55岁"),
    OLD("55岁以上"),
    FEMALE("女"),
    MALE("男"),
    GENDER("性别"),
    AGE("年龄"),
    GLASSES("眼镜"),
    SUNGLASS("墨镜"),
    HAT("帽子"),
    CALLING("打电话"),
    MASK("口罩"),
    HAIRSTYLE("发型"),
    BEARD("胡须"),
    SMILEY_FACE("笑脸"),
    SKIN_COLOR("肤色"),
    BEARD_STYLE("胡须风格"),
    HAT_COLOR("帽子颜色"),
    SHAVEN("剃须"),
    BALD("秃头"),
    BUZZCUT("超短发"),
    SHORT("短发"),
    MEDIUM("中发"),
    TRESS("长发"),
    SNOOD("束发网"),
    NORMAL("正常"),
    SMILE("微笑"),
    LAUGH("大笑"),
    WHITE("白色"),
    BLACK("黑色"),
    YELLOW("黄色"),
    BROWN("棕色"),
    RED_WHITE("红白色"),
    OTHER("其它"),
    MUSTACHE("八字胡"),
    LIGHT_BEARD("浅胡须"),
    YES("是"),
    NO("否"),
    UNKNOWN("未知");


    public static String getTypeName(String key) {
        try {
            return TypeEnum.valueOf(key).getTypeName();
        } catch (Exception e) {
            return UNKNOWN.getTypeName();
        }
    }

    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    TypeEnum(String typeName) {
        this.typeName = typeName;
    }
}
