package org.jim.core.packets;

public enum UserStatusType {
    /**
     * <pre>
     * 在线
     * </pre>
     *
     * <code>ONLINE = 0;</code>
     */
    ONLINE(0, "online", "在线"),
    /**
     * <pre>
     *离线
     * </pre>
     *
     * <code>OFFLINE = 1;</code>
     */
    OFFLINE(1, "offline", "离线"),
    /**
     * <pre>
     * ALL所有(在线+离线)
     * </pre>
     *
     * <code>ALL = 2;</code>
     */
    ALL(2, "all", "所有");


    public final int getNumber() {
        return value;
    }

    public static UserStatusType valueOf(int value) {
        return forNumber(value);
    }

    public static UserStatusType forNumber(int value) {
        switch (value) {
            case 0: return ONLINE;
            case 1: return OFFLINE;
            case 2: return ALL;
            default: return null;
        }
    }
    private final int value;

    private final String status;

    private final String desc;

    UserStatusType(int value, String status, String desc) {
        this.value = value;
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
