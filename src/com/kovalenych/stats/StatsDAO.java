package com.kovalenych.stats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsDAO {


    public Cursor sessionsCursor;
    private SQLiteDatabase database;
    private StatsDBHelper dbOpenHelper;
    private Context context;
    private String tableName;

    public static final int BREATH_FINISHED = 0;
    public static final int HOLD_FINISHED = 1;
    public static final int CONTRACTION = 2;

    public StatsDAO(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
        init();
    }

    // Инициализация адаптера: открываем базу и создаем курсор
    private void init() {
        dbOpenHelper = new StatsDBHelper(context);
        try {
            database = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            // /Если база не открылась, то дальше нам дороги нет
            // но это особый случай
            Log.e(this.toString(), "Error while getting database");
            throw new Error("The end");
        }
    }

    /**
     * Session table                                    CycleEvent
     * id   atable_name   starttime   endtime  comment     !   id  session_id   n   event_type   time
     */
    long curSessionId;

    public void onStartSession() {
        ContentValues values = new ContentValues();
        values.put(StatsDBHelper.C_START_TIME, System.currentTimeMillis());
        values.put(StatsDBHelper.C_COMMENT, "no comment");
        curSessionId = database.insert(StatsDBHelper.SESSIONS_TABLE, null, values);
        Log.d("cursessionid ", curSessionId + "");
    }

    public void onEndSession() {
        ContentValues values = new ContentValues();
        values.put(StatsDBHelper.C_END_TIME, System.currentTimeMillis());
        database.update(StatsDBHelper.SESSIONS_TABLE, values,
                StatsDBHelper.C_ID + "=?", new String[]{curSessionId + ""});
    }

    public void onContraction() {

    }

    public void onCycleLife(boolean isBrething, int numberOdCycle, int time) {
        ContentValues values = new ContentValues();
        values.put(StatsDBHelper.C_SESSION, curSessionId);
        values.put(StatsDBHelper.C_CYCLE_NUM, numberOdCycle);
        values.put(StatsDBHelper.C_EVENT_TYPE, isBrething ? BREATH_FINISHED : HOLD_FINISHED);
        values.put(StatsDBHelper.C_EVENT_TIME, time);
        curSessionId = database.insert(StatsDBHelper.CYCLE_EVENTS_TABLE, null, values);
    }

    public long getItemId(int position) {
        Session nameOnPosition = getItem(position);
        return nameOnPosition.getId();
    }

    public Session getItem(int position) {
        if (sessionsCursor.moveToPosition(position)) {
            long id = sessionsCursor.getLong(0);
            Session siteOnPositon = new Session(id);
            siteOnPositon.start = sessionsCursor.getLong(sessionsCursor.getColumnIndex(StatsDBHelper.C_START_TIME));
            siteOnPositon.end = sessionsCursor.getLong(sessionsCursor.getColumnIndex(StatsDBHelper.C_END_TIME));
            siteOnPositon.comment = sessionsCursor.getString(sessionsCursor.getColumnIndex(StatsDBHelper.C_COMMENT));
            return siteOnPositon;
        } else {
            throw new CursorIndexOutOfBoundsException(
                    "Cant move cursor to postion");
        }
    }


    //Прочие служебные методыв
    //todo: explicitly
    public void onDestroy() {
        sessionsCursor.close();
        dbOpenHelper.close();
    }

    //Вызывает обновление вида
    private void refresh() {
        sessionsCursor = getAllSessions();
    }

    public Cursor getAllSessions() {
        //Список колонок базы, которые следует включить в результат

        // составляем запрос к базе
        return database.query(StatsDBHelper.SESSIONS_TABLE, new String[]{},
                null, null, null, null, StatsDBHelper.C_ID);
    }
    //TODO: close cursor!!!


    public int getSessionsCount() {
        return sessionsCursor.getCount();
    }


}
