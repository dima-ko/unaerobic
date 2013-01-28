package com.kovalenych;

import android.provider.BaseColumns;

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
    public final static String FLAG_SETVOLUME = "set_volume";
    public final static String FLAG_CONTRACTION = "contraction";
    public final static String FLAG_IMMEDIATE_BREATH = "immediate_breath";

    public final static String PARAM_CYCLES = "cycles";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_TABLE = "table_name";
    public final static String PARAM_VOLUME = "volume";
    public final static String PARAM_M_CYCLE = "multi_cycles_number";
    public final static String PARAM_PROGRESS = "progress";
    public final static String PARAM_WHOLE_TABLE_ELAPSED = "whole_table_last";
    public final static String PARAM_WHOLE_TABLE_REMAINS= "whole_table_remains";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_BREATHING = "breathing";

    public static final String C_ID = BaseColumns._ID;
    public static final String C_ATABLE_NAME = "atable_name";
    public static final String C_START_TIME = "start_time";
    public static final String C_END_TIME = "end_time";
    public static final String C_COMMENT = "comment";

    public static final String C_SESSION = "session";
    public static final String C_CYCLE_NUM = "cycle_num";
    public static final String C_EVENT_TYPE = "event_type";
    public static final String C_EVENT_TIME = "event_time";

    public static final int BREATH_FINISHED = 0;
    public static final int HOLD_FINISHED = 1;
    public static final int CONTRACTION = 2;


    public static final int STATUS_BREATH = 1;
    public static final int STATUS_HOLD = 2;
    public static final int STATUS_FINISH = 3;

    public static final int SUBSCRIBER_CLOCK = 1;
    public static final int SUBSCRIBER_CYCLE = 2;
    public static final int SUBSCRIBER_TABLE = 3;
}
