package com.trinhdan.easywriter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class FreeWriteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "freewrites.db";
    private static final int VERSION = 1;
    private Context context;

    public FreeWriteDatabaseHelper(Context context){
        super(context, DB_NAME, null, VERSION);
        this.context = context;
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
        // genre column
        public static final String COL_GENRE = "genre";
        // the story pictures that the user landed on; saved as a delimited string
        public static final String COL_STORY_PICS = "story_pics";
        // .txt filepath on device of the freewrite
        public static final String COL_FILEPATH = "filepath";
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
                FreewriteTable.COL_GENRE + " text, " +
                FreewriteTable.COL_STORY_PICS + " text, " +
                FreewriteTable.COL_FILEPATH + " text, " +
                FreewriteTable.COL_DURATION + " integer," +
                FreewriteTable.COL_FAV + " integer" +
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: For future update: Add logic to save the existing data to a collection before wiping it.
        // Then, when creating the new database, insert that old data.
        db.execSQL("drop table if exists " + FreewriteTable.TABLE);
        onCreate(db);
    }


    /**
     * Seeds the database with some default information. For debugging; to be removed later.
     */
//    private void seed(){
////        FreeWrite fw1 = new FreeWrite(0, new Date(), "Freewrite #01", "fake_link", "", 0, false);
////        FreeWrite fw2 = new FreeWrite(1, new Date(), "Freewrite #02", "fake_link", "", 0, false);
////
////        FreeWriteDAO.getInstance(context).insertFreeWrite(fw1);
////        FreeWriteDAO.getInstance(context).insertFreeWrite(fw2);
//    }
}
