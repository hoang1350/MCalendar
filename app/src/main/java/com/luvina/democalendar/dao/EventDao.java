package com.luvina.democalendar.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.luvina.democalendar.model.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling data access to database
 */
public class EventDao extends SQLiteOpenHelper {
    private static final String TAG = "EventDao";
    // Declare database name
    private static final String DB_NAME = "event.database";
    // Declare the version of DB
    private static final int VERSION = 1;

    /**
     * Create a database
     *
     * @param context: context
     * @author HoangNN
     */
    public EventDao(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * Create a table "event" in database
     *
     * @param db: object SQLiteDatabase
     * @throws SQLException
     * @author HoangNN
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE event");
            sql.append(" (id INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sql.append("name TEXT, start_date DATETIME, end_date DATETIME, note TEXT, image TEXT, notify INTEGER)");
            db.execSQL(sql.toString());
        } catch (SQLException e) {
            Log.d(TAG, getClass().getName() + " onCreate" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Insert a record into table "event"
     *
     * @param event: object event
     * @throws SQLException
     * @return: return a rowId of a record after inserting
     * @author HoangNN
     */
    public int addEvent(EventModel event) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int rowId = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO event (name, start_date, end_date, note, image, notify) ");
            sql.append("VALUES (?, ?, ?, ?, ?, ?) ");
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql.toString());
            int index = 1;
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(index++, event.getName());
            sqLiteStatement.bindString(index++, event.getStartDate());
            sqLiteStatement.bindString(index++, event.getEndDate());
            sqLiteStatement.bindString(index++, event.getNote());
            sqLiteStatement.bindString(index++, event.getImage());
            sqLiteStatement.bindLong(index++, event.getNotify());
            // executeInsert
            rowId = (int) sqLiteStatement.executeInsert();
        } catch (SQLException e) {
            Log.d(TAG, getClass().getName() + " addEvent" + e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
        return rowId;
    }

    /**
     * Update a record from table "event"
     *
     * @param event: object event
     * @throws SQLException
     * @return: return true if update success, false if not
     * @author HoangNN
     */
    public boolean updateEvent(EventModel event) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int rowUpdated = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE event SET name = ?, start_date = ?, end_date = ?, note = ?, image = ?, notify = ? ");
            sql.append("WHERE id = ?");
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql.toString());
            int index = 1;
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(index++, event.getName());
            sqLiteStatement.bindString(index++, event.getStartDate());
            sqLiteStatement.bindString(index++, event.getEndDate());
            sqLiteStatement.bindString(index++, event.getNote());
            sqLiteStatement.bindString(index++, event.getImage());
            sqLiteStatement.bindLong(index++, event.getNotify());
            sqLiteStatement.bindLong(index++, event.getId());
            // executeUpdate
            rowUpdated = sqLiteStatement.executeUpdateDelete();
        } catch (SQLException e) {
            Log.d(TAG, getClass().getName() + " addEvent" + e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
        return rowUpdated > 0;
    }

    /**
     * Get all records in table "event"
     *
     * @return List<EventModel>: return a list event
     * @author HoangNN
     */
    public List<EventModel> getListEvent() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Initialize an ArrayList
        List<EventModel> listEvent = new ArrayList<>();
        String tableName = "event";
        // query data
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        // Loop the cursor
        while (cursor.moveToNext()) {
            // For each record, initialize an event and add to the list
            EventModel event = new EventModel();
            event.setId(cursor.getInt(cursor.getColumnIndex("id")));
            event.setName(cursor.getString(cursor.getColumnIndex("name")));
            event.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
            event.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
            event.setImage(cursor.getString(cursor.getColumnIndex("image")));
            event.setNote(cursor.getString(cursor.getColumnIndex("note")));
            event.setNotify(cursor.getInt(cursor.getColumnIndex("notify")));
            listEvent.add(event);
        }
        sqLiteDatabase.close();
        return listEvent;
    }

    /**
     * Get list event with condition: the given date
     *
     * @param dateStr: the date String format: yyyy-MM-dd
     * @return List<EventModel>: return a list event
     * @author HoangNN
     */
    public List<EventModel> getListEvent(String dateStr) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Initialize an ArrayList
        List<EventModel> listEvent = new ArrayList<>();
        // table name
        String tableName = "event";
        // columns to return
        String[] columnsReturn = {"id", "name", "start_date", "end_date", "note", "image", "notify"};
        // where condition
        String selection = "? BETWEEN DATE(start_date) AND DATE(end_date)";
        // value for param "?"
        String[] selectionValue = {dateStr};
        // the sort column
        String orderBy = "start_date";
        // query data
        Cursor cursor = sqLiteDatabase.query(tableName, columnsReturn, selection, selectionValue, null, null, orderBy);
        // Loop the cursor
        while (cursor.moveToNext()) {
            // For each record, initialize an event and add to the list
            EventModel event = new EventModel();
            event.setId(cursor.getInt(cursor.getColumnIndex("id")));
            event.setName(cursor.getString(cursor.getColumnIndex("name")));
            event.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
            event.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
            event.setImage(cursor.getString(cursor.getColumnIndex("image")));
            event.setNote(cursor.getString(cursor.getColumnIndex("note")));
            event.setNotify(cursor.getInt(cursor.getColumnIndex("notify")));
            listEvent.add(event);
        }
        sqLiteDatabase.close();
        return listEvent;
    }

    /**
     * Check if a particular event is conflict with the other events
     *
     * @param event: object event
     * @return: return true if conflict, false if not
     * @author HoangNN
     */
    public boolean checkTimeEvent(EventModel event) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // table name
        String tableName = "event";
        // columns to return
        String[] columnsReturn = {"id"};
        // where condition
        String selection = "(? BETWEEN start_date AND end_date) OR (? BETWEEN start_date AND end_date) OR (? <= start_date AND ? >= end_date)";
        // value for param "?"
        String[] selectionValue = {event.getStartDate(), event.getEndDate(), event.getStartDate(), event.getEndDate()};
        // Case update, add a condition: id != id of the current editing event
        if (event.getId() > 0) {
            selection = "((? BETWEEN start_date AND end_date) OR (? BETWEEN start_date AND end_date) OR (? <= start_date AND ? >= end_date)) AND id != ?";
            selectionValue = new String[]{event.getStartDate(), event.getEndDate(), event.getStartDate(), event.getEndDate(), String.valueOf(event.getId())};
        }
        // query data
        Cursor cursor = sqLiteDatabase.query(tableName, columnsReturn, selection, selectionValue, null, null, null);
        // If exists a record
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }
}
