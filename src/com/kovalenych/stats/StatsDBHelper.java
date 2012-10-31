package com.kovalenych.stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsDBHelper extends SQLiteOpenHelper {

    public static final String SESSIONS_TABLE = "sessions";
    public static final String CYCLE_EVENTS_TABLE = "cycle_events";
    public static final String FILE_NAME = "stats.db";
    private static final int DB_VERSION = 1;

    public static final String C_ID = BaseColumns._ID;
    public static final String C_ATABLE_NAME = "atable_name";
    public static final String C_START_TIME = "start_time";
    public static final String C_END_TIME = "end_time";
    public static final String C_COMMENT = "comment";

    public static final String C_SESSION = "session";
    public static final String C_CYCLE_NUM = "cycle_num";
    public static final String C_EVENT_TYPE = "event_type";
    public static final String C_EVENT_TIME = "event_time";

    public StatsDBHelper(Context context) {
        super(context, FILE_NAME, null, DB_VERSION);
    }

    /**
     * Session table                                    CycleEvent
     * id   atable_name   starttime   endtime  comment     !   id  session_id   n   event_type   time
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS " + SESSIONS_TABLE +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT)",
                C_ID, C_ATABLE_NAME, C_START_TIME, C_END_TIME, C_COMMENT);
        db.execSQL(sql);

        String sql2 = String.format("CREATE TABLE IF NOT EXISTS " + CYCLE_EVENTS_TABLE +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
                C_ID, C_ATABLE_NAME, C_START_TIME, C_END_TIME, C_COMMENT);
        db.execSQL(sql2);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
