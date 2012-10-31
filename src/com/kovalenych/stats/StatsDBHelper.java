package com.kovalenych.stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.kovalenych.Const;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsDBHelper extends SQLiteOpenHelper implements Const {

    public static final String SESSIONS_TABLE = "sessions";
    public static final String CYCLE_EVENTS_TABLE = "cycle_events";
    public static final String FILE_NAME = "stats.db";
    private static final int DB_VERSION = 1;



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
