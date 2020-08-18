package com.herocheer.zhsq.localservice.core.device.box.YTBox.dict;

/**
 * @author wind
 * @date 2020/1/7 16:31
 */
public enum TypeValueEnum {
    UNKNOWN(0),
    /**
     * 笑脸
     */
    NORMAL(1),
    SMILE(2),
    LAUGH(3),
    /**
     * 胡子类型
     */
    BEARD(1),
    MUSTACHE(2),
    LIGHT_BEARD(3),
    /**
     * 颜色: 肤色, 帽子颜色 ...
     */
    WHITE(1),
    BLACK(2),
    YELLOW(3),
    BROWN(4),
    RED_AND_WHITE(5),
    OTHER(99);

    public static Integer getTypeValue(String key) {
        try {
            return TypeValueEnum.valueOf(key).getTypeValue();
        } catch (Exception e) {
            return UNKNOWN.getTypeValue();
        }
    }

    private Integer typeValue;

    public Integer getTypeValue() {
        return typeValue;
    }

    TypeValueEnum(Integer typeValue) {
        this.typeValue = typeValue;
    }
}
