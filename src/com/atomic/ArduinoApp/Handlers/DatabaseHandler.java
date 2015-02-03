package com.atomic.ArduinoApp.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.atomic.ArduinoApp.BaseActivity;
import com.atomic.ArduinoApp.Intelligence.IntelligenceCore;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ArduinoApp.db";

    public static final String TABLE_NAME_CONFIG = "config";
    public static final String TABLE_NAME_TIMESTAMP = "timestamp";

    public String TAG = "DatabaseHandler";
    private boolean DEBUG = false;

    private BaseActivity activity = null;

    public DatabaseHandler(Context context, BaseActivity activity) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.activity = activity;
        if(DEBUG) if(DEBUG) Log.d(TAG, "DATABASEHADLER CALLED!");
        onCreate(this.getWritableDatabase());
    }

    public void onCreate(SQLiteDatabase database) {
        if(DEBUG) Log.d(TAG,"ONCREATE HAS BEEN CALLED");
        String query_config = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME_CONFIG + "`" +
                "(" +
                " `id` varchar(255) PRIMARY KEY," +
                " `name` varchar(255)," +
                " `value` varchar(255)," +
                " `type` varchar(255)," +
                " `unit` varchar(255)" +
                ");";
        database.execSQL(query_config);

        String query_timestamp = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME_TIMESTAMP + "`" +
                "(" +
                " `id` integer PRIMARY KEY," +
                " `timestamp` varchar(255)," +
                " `action` varchar(255)" +
                ");";

        database.execSQL(query_timestamp);

        if(DEBUG) if(DEBUG) Log.d(TAG, "onCreate() finished.");
    }

    public void onUpgrade(SQLiteDatabase database, int i, int j) {
        /**
         * Not needed function.
         */
    }

    public void add(DatabaseHelper.Type type, String... values) {
        DatabaseHandler db = activity.getDatabaseHandler();
        SQLiteDatabase database = db.getWritableDatabase();

        String table_name = DatabaseHelper.Type.parse(type);


        String[] config_loc = {"name", "value", "type", "unit"};
        String[] timestamp_loc = {"id", "timestamp", "action"};
        String[] using = (table_name.equalsIgnoreCase("config") ? config_loc : timestamp_loc);

        ContentValues value = new ContentValues();
        for(int i = 0; i < values.length; i++) {
            value.put(using[i], values[i]);

            if (DEBUG) if(DEBUG) Log.d(TAG, "Added value: " + values[i] + " to database!");
        }

        long rowID;
        rowID = database.insert(table_name, null, value);

        if(DEBUG) if(DEBUG) Log.d(TAG, "Added Row with ID: " + rowID);
    }

    public String get(DatabaseHelper.Type type, String value_key, String value, String column) {
        DatabaseHandler db = activity.getDatabaseHandler();
        SQLiteDatabase database = db.getReadableDatabase();

        String table_name = DatabaseHelper.Type.parse(type);

        String query = "SELECT `" + column + "` FROM `" + table_name + "` WHERE `" + value_key + "` = " + value + ";";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        if(DEBUG) if(DEBUG) Log.d(TAG, "Found cursor element!");
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public String values(DatabaseHelper.Type type, String column) {
        DatabaseHandler db = activity.getDatabaseHandler();
        SQLiteDatabase database = db.getReadableDatabase();

        String table_name = DatabaseHelper.Type.parse(type);

        String query = "SELECT `"+ column +"` FROM `" + table_name + "`;";

        Cursor cursor = database.rawQuery(query,null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor.getString(cursor.getColumnIndex(column));
    }

    public int getIdentifcationNumber(DatabaseHelper.Type type) {

        DatabaseHandler db = activity.getDatabaseHandler();
        SQLiteDatabase database = db.getReadableDatabase();

        String table_name = DatabaseHelper.Type.parse(type);

        String query = "SELECT `id` FROM `" + table_name + "`;";

        Cursor cursor = database.rawQuery(query, null);
        int i = -1;
        if(cursor.moveToFirst()) {
            i = cursor.getCount();
        }

        return i;
    }

    public void dropTable(DatabaseHelper.Type type) {
        DatabaseHandler db = activity.getDatabaseHandler();
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "DROP TABLE `" + DatabaseHelper.Type.parse(type) + "`;";

        database.execSQL(query);

        Log.w(TAG, "Database deleted!");

        onCreate(database);
    }
}
