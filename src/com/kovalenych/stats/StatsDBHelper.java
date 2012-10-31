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

/**
*
*                 Session table                                    CycleEvent
*   id   atable_name   starttime   endtime  comment     !   id  session   n   event_type   time
*                                                       !
*                                                       !
*                                                       !
*                                                       !
*                                                       !
*                                                       !
*
*
*
*
*
*
*
*/












    public static final String VIDEO_TABLE = "video";
    public static final String ARTICLES_TABLE = "articles";
    public static final String FILE_NAME = "media.db";
    private static final int DB_VERSION = 1;


    public static final String C_ID = BaseColumns._ID;

    public static final String C_ART_NAME = "article_name";
    public static final String C_ART_URL = "article_url";
    public static final String C_ART_AUTHOR = "article_author";

    public static final String C_VIDEO_NAME = "video_name";
    public static final String C_VIDEO_URL = "video_url";

    public StatsDBHelper(Context context) {
        super(context, FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS " + ARTICLES_TABLE + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT , %s TEXT)", C_ID, C_ART_NAME, C_ART_URL, C_ART_AUTHOR);
        db.execSQL(sql);

        String sql2 = String.format("CREATE TABLE IF NOT EXISTS " + VIDEO_TABLE + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)", C_ID, C_VIDEO_NAME, C_VIDEO_URL);
        db.execSQL(sql2);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
