package com.kovalenych.stats;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsDAO {

    private Cursor sessionsCursor;
    private SQLiteDatabase database;
    private StatsDBHelper dbOpenHelper;
    private Context context;
    private String tableName;

    public StatsDAO(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
        init();
    }

    /**
     * Session table                                    CycleEvent
     * id   atable_name   starttime   endtime  comment     !   id  session_id   n   event_type   time
     */
    int curSessionId;

    public void onEndSession() {

    }

    public void onContraction() {

    }

    public void onCycleLife(boolean isBrething, int time) {

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

    public int getSessionsCount() {
        return sessionsCursor.getCount();
    }


}
