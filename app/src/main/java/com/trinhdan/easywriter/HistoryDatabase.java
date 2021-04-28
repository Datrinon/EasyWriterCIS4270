package com.trinhdan.easywriter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "freewrites.db";
    private static final int VERSION = 1;

    public HistoryDatabase(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    public static final class FreewriteTable {
        // name of the table
        public static final String TABLE = "free_writes";
        // id of the freewrite
        public static final String COL_ID = "_id";
        // timestamp the freewrite was created
        public static final String COL_DATE = "date";
        // title given by the user to the freewrite
        public static final String COL_TITLE = "title";
        // .txt filepath on device of the freewrite
        public static final String COL_LINK = "filepath";
        // duration of the freewrite
        public static final String COL_DURATION = "time_taken";
        // did the user mark it as a favorite?
        public static final String COL_FAV = "favorite";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FreewriteTable.TABLE + " (" +
                FreewriteTable.COL_ID  + " integer primary key autoincrement," +
                FreewriteTable.COL_DATE + " text, " + // no datetime datatype in sqlite, store as text
                FreewriteTable.COL_TITLE + " text, " +
                FreewriteTable.COL_LINK + " text, " +
                FreewriteTable.COL_DURATION + " float," +
                FreewriteTable.COL_FAV + " integer" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Add logic to save the existing data to a collection before wiping it.
        // Then, when creating the new database, insert that old data.
        db.execSQL("drop table if exists " + FreewriteTable.TABLE);
        onCreate(db);
    }

    // Add methods to UPDATE and DELETE VALUES from the data.


}
