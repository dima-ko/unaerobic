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

    public static final String REQUESTS_TABLE = "requests";
    public static final String RECORDS_TABLE = "records";
    public static final String RECORDS_DB = "records.db";
    public static final String REQUESTS_DB = "savedquery.db";
    public String fileName;
    private static final int DB_VERSION = 1;


    public static final String C_ID = BaseColumns._ID;
    public static final String C_NAME = "name";
    public static final String C_COUNTRY = "country";
    public static final String C_RESULT = "result";
    public static final String C_TABLENAME = "tablename";
    public static final String C_LASTUPD = "lastupd";
    public static final String C_FILTER = "filter";


    public DBHelper(Context context,String filename) {
        super(context, RECORDS_DB, null, DB_VERSION);
        fileName = filename;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        if (fileName.equals(REQUESTS_DB))
            sql = String.format("CREATE TABLE IF NOT EXISTS " + REQUESTS_TABLE + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)", C_ID, C_TABLENAME, C_LASTUPD);
        else
            sql = String.format("CREATE TABLE IF NOT EXISTS " + RECORDS_TABLE + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT , %s TEXT, %s TEXT)", C_ID, C_NAME, C_COUNTRY, C_RESULT, C_FILTER);
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ REQUESTS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ RECORDS_TABLE);
        onCreate(sqLiteDatabase);

    }
}
