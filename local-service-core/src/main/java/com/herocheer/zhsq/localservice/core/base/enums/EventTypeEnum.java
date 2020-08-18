package com.herocheer.zhsq.localservice.core.base.enums;

public enum EventTypeEnum {
    MEN_DOWN("倒地事件"),
    HANDS_UP("求救事件"),
    HAS_PEOPLE("闯入事件"),
    HAS_FIGHT("打架事件"),
    HAS_FIRE("消防事件"),
    HAS_CROWD("聚众事件"),
    HAS_CLIMB("攀爬事件"),
    NO_PEOPLE("离岗事件"),
    OUT_OF_BED("离床事件"),
    TOO_HIGH("攀高事件"),
    WC_FOLLOW("入厕尾随"),
    HAS_FLAME("火焰检测"),
    HAS_SMOKE("烟雾检测"),
    HAS_COLOR("颜色检测"),
    WC_TIMEOUT("入厕超时"),
    TOO_NOISY("喧哗检测"),
    HAS_SLEEP("睡岗检测"),
    HAS_WANDER("徘徊检测"),
    HAS_RETENTION("滞留检测"),
    HAS_LESS("独处检测"),
    HAS_EQUAL("单人提审"),
    DBL_GUARD("警戒检测"),
    DBL_GUARD_PHASE_(""),
    DIRECTION_ALERT("逆行检测"),
    DIRECTION_ALERT_PHASE_A(""),
    HAS_ALONE("独处检测"),
    HAS_ABSENCE("缺岗事件"),
    HAS_LEFT("离开事件");

    private String value;

    EventTypeEnum(String value) {
        this.value = value;
    }

    public static EventTypeEnum getValuesByType(String type){
        EventTypeEnum[] values= EventTypeEnum.values();
        for (EventTypeEnum e : values) {
            if(type.equals(e.name())){
                return e;
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }
}
