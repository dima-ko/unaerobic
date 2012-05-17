package com.kovalenych.ranking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class DBHelper extends SQLiteOpenHelper {

    public  String tableName = "records";
    private static final int DB_VERSION = 1;


    public static final String C_ID = BaseColumns._ID;
    public static final String C_NAME = "name";
    public static final String C_COUNTRY = "country";
    public static final String C_RESULT = "result";


    public DBHelper(Context context) {
        super(context, "timeline.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table " + tableName + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,%s TEXT ,%s TEXT )", C_ID, C_NAME, C_COUNTRY, C_RESULT);
        db.execSQL(sql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
