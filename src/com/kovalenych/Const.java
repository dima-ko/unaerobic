package com.kovalenych;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public interface Const {
    public final static String FLAG = "flag";

    public final static String FLAG_CREATE = "create";
    public final static String FLAG_SUBSCRIBE_TABLE = "subscribe_tables";
    public final static String FLAG_SUBSCRIBE_CYCLES = "subscribe_cycles";
    public final static String FLAG_SHOW_TRAY = "showtray";
    public final static String FLAG_HIDE_TRAY = "hidetray";
    public final static String FLAG_CLICK_BREATH = "click_breath";
    public final static String FLAG_CLICK_HOLD = "click_hold";

    public final static String PARAM_CYCLES = "cycles";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_TABLE = "table_name";
    public final static String PARAM_CYCLE_NUM = "cycle_num";
    public final static String PARAM_PROGRESS = "progress";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_BREATHING = "breathing";


    public static final int STATUS_BREATH = 1;
    public static final int STATUS_HOLD = 2;
    public static final int STATUS_FINISH = 3;

    public static final int SUBSCRIBER_CLOCK = 1;
    public static final int SUBSCRIBER_CYCLE = 2;
    public static final int SUBSCRIBER_TABLE = 3;
}
