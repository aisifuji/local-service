package com.herocheer.ytbox.demo.dict;

/**
 * @author wind
 * @date 2019/11/22 16:33
 */
public enum GenderEnum {
    /**
     * 0: 女, 1:男, -1:未知
     */
    FEMALE(0), MALE(1), UNKNOWN(-1);

    public static int getSex(String key) {
        try {
            return GenderEnum.valueOf(key).getSex();
        } catch (Exception e) {
            return UNKNOWN.getSex();
        }
    }

    GenderEnum(int sex) {
        this.sex = sex;
    }

    private int sex;

    public int getSex() {
        return sex;
    }
}
