package com.kovalenych.stats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.kovalenych.Const;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsDAO implements Const {


    private Cursor sessionsCursor;
    private SQLiteDatabase database;
    private StatsDBHelper dbOpenHelper;
    private Context context;
    private String tableName;
    private boolean inService;

    public StatsDAO(Context context, String tableName, boolean inService) {
        this.context = context;
        this.tableName = tableName;
        this.inService = inService;
        init();
        if (!inService)
            refresh();
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
        values.put(C_ATABLE_NAME, tableName);
        values.put(C_START_TIME, System.currentTimeMillis());
        values.put(C_COMMENT, "long click to \nadd comment");
        curSessionId = database.insert(StatsDBHelper.SESSIONS_TABLE, null, values);
        Log.d("cursessionid ", curSessionId + "");
    }

    public void onEndSession() {
        ContentValues values = new ContentValues();
        values.put(C_END_TIME, System.currentTimeMillis());
        database.update(StatsDBHelper.SESSIONS_TABLE, values,
                C_ID + "=?", new String[]{curSessionId + ""});
    }

    public void onContraction() {

    }

    public void onCycleLife(boolean isBrething, int numberOdCycle, int time) {
        ContentValues values = new ContentValues();
        values.put(C_SESSION, curSessionId);
        values.put(C_CYCLE_NUM, numberOdCycle);
        values.put(C_EVENT_TYPE, isBrething ? BREATH_FINISHED : HOLD_FINISHED);
        values.put(C_EVENT_TIME, time);
        database.insert(StatsDBHelper.CYCLE_EVENTS_TABLE, null, values);
    }

    public long getItemId(int position) {
        Session nameOnPosition = getItem(position);
        return nameOnPosition.getId();
    }

    public Session getItem(int position) {
        if (sessionsCursor.moveToPosition(position)) {
            long id = sessionsCursor.getLong(0);
            Session siteOnPositon = new Session(id);
            siteOnPositon.start = sessionsCursor.getLong(sessionsCursor.getColumnIndex(C_START_TIME));
            siteOnPositon.end = sessionsCursor.getLong(sessionsCursor.getColumnIndex(C_END_TIME));
            siteOnPositon.comment = sessionsCursor.getString(sessionsCursor.getColumnIndex(C_COMMENT));
            return siteOnPositon;
        } else {
            throw new CursorIndexOutOfBoundsException(
                    "Cant move cursor to postion");
        }
    }

    public void onDestroy() {
        if (!inService)
            sessionsCursor.close();
        dbOpenHelper.close();
    }

    //Вызывает обновление вида
    public void refresh() {
        sessionsCursor = getAllSessionsOfTable();
        Log.d("stats getView", "getAllSessionsOfTable()" + sessionsCursor.getCount());
    }

    public Cursor getAllSessionsOfTable() {
        //Список колонок базы, которые следует включить в результат

        // составляем запрос к базе
        return database.query(StatsDBHelper.SESSIONS_TABLE,
                new String[]{C_ID, C_START_TIME, C_END_TIME, C_COMMENT},
                C_ATABLE_NAME + " like " + "'%" + tableName + "%'",
                null, null, null, C_ID);
    }

    public Cursor getSessionTimeLine(long SessionId) {
        //Список колонок базы, которые следует включить в результат

        // составляем запрос к базе
        return database.query(StatsDBHelper.CYCLE_EVENTS_TABLE,
                new String[]{C_ID, C_CYCLE_NUM, C_EVENT_TYPE, C_EVENT_TIME},
//                C_SESSION + "=?", new String[]{SessionId + ""},
                null, null,
                null, null,
                C_ID);
    }

    public int getSessionsCount() {
        return sessionsCursor.getCount();
    }


}
