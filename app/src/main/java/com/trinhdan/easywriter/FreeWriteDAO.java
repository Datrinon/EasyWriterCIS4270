package com.trinhdan.easywriter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
    A data access object that manages and simplifies interactions with the database.
    Allows the user to create, read, update, and delete FreeWrite data.

    Adheres to singleton pattern seen in Section 5.3 - Band Database App
 */
public class FreeWriteDAO {
// note: in 3150 we learnt that a DAO was implemented with an interface...
// but I feel that is not necessary given there is only one class to manage the access to the freewrite db table.

    // for working with database. the object itself, the helper, and the db.
    private static FreeWriteDAO freeWriteDAO;
    private FreeWriteDatabaseHelper dbHelper;
    private SQLiteDatabase db;


    public static FreeWriteDAO getInstance(Context context){
        if (freeWriteDAO == null) {
            freeWriteDAO = new FreeWriteDAO(context); // The constructor (private) is exposed by freeWriteDAO.
        }
        return freeWriteDAO;
    }

    private FreeWriteDAO(Context context){
        dbHelper = new FreeWriteDatabaseHelper(context);
        db = dbHelper.getWritableDatabase(); // a 'writable' database can be used for reading and writing.
    }

    /**
     * Gets all free write entries from the database sorted by the most recent one.
     * @return A list of the user's freewrites.
     */
    public List<FreeWrite> fetchAllFreeWrites() {
        List<FreeWrite> fwList = new ArrayList<>();
        String sql = "SELECT * FROM " + FreeWriteDatabaseHelper.FreewriteTable.TABLE + " ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        try {
            do {
                int id = cursor.getInt(0);
                Date date =  FreeWrite.DATE_FORMAT.parse(cursor.getString(1));
                String title = cursor.getString(2);
                String storyPic = cursor.getString(3);
                String link = cursor.getString(4);
                double duration = cursor.getFloat(5);
                boolean favorite = cursor.getInt(6) == 1 ? true : false;

                fwList.add(new FreeWrite(id, date, title, storyPic, link, duration, favorite));
            } while (cursor.moveToNext());
        } catch(SQLiteException | ParseException | CursorIndexOutOfBoundsException e){
            Log.e("Database Error", e.getMessage());
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return fwList;
    }

    /**
     * Insert a free write record into the database. R
     */
    public void insertFreeWrite(FreeWrite freeWrite) {
        ContentValues fwValues = new ContentValues();
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_TITLE, freeWrite.getTitle());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_DATE, freeWrite.getDateAsString());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_DURATION, freeWrite.getDuration());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_FAV, freeWrite.isFavorite());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_LINK, freeWrite.getLink());

        db.insert(FreeWriteDatabaseHelper.FreewriteTable.TABLE, null, fwValues);
    }



}
