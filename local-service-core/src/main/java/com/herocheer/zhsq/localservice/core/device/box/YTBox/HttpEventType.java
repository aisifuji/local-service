package com.herocheer.zhsq.localservice.core.device.box.YTBox;

public enum HttpEventType {
    FACE_RESULT(1);

    private final int value;

    HttpEventType(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     * @return null if the value is not found.
     */
    public static HttpEventType findByValue(int value) {
        switch (value) {
            case 1:
                return FACE_RESULT;
            default:
                return null;
        }
    }
}
