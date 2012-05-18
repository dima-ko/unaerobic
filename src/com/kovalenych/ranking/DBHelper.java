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

    public static final String RECORDS_CONF = "recordsconf";
    public String tableName;
    private static final int DB_VERSION = 1;


    public static final String C_ID = BaseColumns._ID;
    public static final String C_NAME = "name";
    public static final String C_COUNTRY = "country";
    public static final String C_RESULT = "result";
    public static final String C_TABLENAME = "tablename";
    public static final String C_LASTUPD = "lastupd";


    public DBHelper(Context context, String tableName) {
        super(context, "timeline.db", null, DB_VERSION);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        if (tableName.equals(RECORDS_CONF))
            sql = String.format("CREATE TABLE  " + tableName + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,%s TEXT)", C_ID, C_TABLENAME, C_LASTUPD);
        else
            sql = String.format("CREATE TABLE " + tableName + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,%s TEXT ,%s TEXT)", C_ID, C_NAME, C_COUNTRY, C_RESULT);
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
