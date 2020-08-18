package com.herocheer.zhsq.localservice.core.device.box.YTBox.dict;

/**
 * @author wind
 * @date 2019/11/16 15:57
 */
public enum AgeEnum {
    /**
     * 小于 15 岁, 感受上是不大于初中生
     */
    KID(1, 15),
    /**
     * 大于 15 岁小于 20 岁
     */
    KID_YOUTH(15, 20),
    /**
     * 大于 20 岁小于 30 岁, 是刚毕业工作不久的大学生
     */
    YOUTH(20, 30),
    /**
     * 大于 30 岁小于 35 岁
     */
    YOUTH_ADULT(30, 35),
    /**
     * 大于 35 岁小于 45 岁, 大叔大妈
     */
    ADULT(35, 45),
    /**
     * 大于 45 岁小于 55 岁
     */
    ADULT_OLD(45, 55),
    /**
     * 大于 55 岁，爷爷奶奶
     */
    OLD(55, 999),
    /**
     * 未定义
     */
    UNKNOWN(1, 999);


    public static AgeEnum getRange(String key) {
        try {
            return AgeEnum.valueOf(key);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    AgeEnum(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    private int minAge;
    private int maxAge;

}
