package com.trinhdan.easywriter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.trinhdan.easywriter.models.FreeWrite;

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
        db = dbHelper.getWritableDatabase(); // a 'writable' database can be used for reading too, not just writing.
    }

    /**
     * Fetches a certain freeWrite given the ID. Used in the detail view.
     * @param freewriteId - the ID of the free write to obtain.
     * @return - The freeWrite that was found, otherwise database error.
     */
    public FreeWrite fetchFreeWriteByID(int freewriteId) {
        FreeWrite fw = new FreeWrite();
        String sql = "SELECT * FROM " + FreeWriteDatabaseHelper.FreewriteTable.TABLE + " WHERE " + FreeWriteDatabaseHelper.FreewriteTable.COL_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(freewriteId)});
        try {
            if (cursor.moveToFirst()) {
                    int id = cursor.getInt(0);
                    Date date = FreeWrite.DATE_FORMAT.parse(cursor.getString(1));
                    String title = cursor.getString(2);
                    String genre = cursor.getString(3);
                    String storyPic = cursor.getString(4);
                    String filePath = cursor.getString(5);
                    long duration = cursor.getInt(6);
                    boolean favorite = cursor.getInt(7) == 1;

                    fw = new FreeWrite(id, date, title, genre, storyPic, filePath, duration, favorite);
            }
        } catch(SQLiteException | ParseException | CursorIndexOutOfBoundsException e){
            Log.e("Database Error", e.getMessage());
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return fw;
    }


    /**
     * Gets all free write entries from the database sorted by the most recent one.
     * @return A list of the user's freewrites.
     */
    public List<FreeWrite> fetchAllFreeWrites() {
        List<FreeWrite> fwList = new ArrayList<>();
        String sql = "SELECT * FROM " + FreeWriteDatabaseHelper.FreewriteTable.TABLE +
                     " ORDER BY " + FreeWriteDatabaseHelper.FreewriteTable.COL_ID + " DESC";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date date = FreeWrite.DATE_FORMAT.parse(cursor.getString(1));
                    String title = cursor.getString(2);
                    String genre = cursor.getString(3);
                    String storyPic = cursor.getString(4);
                    String filePath = cursor.getString(5);
                    long duration = cursor.getInt(6);
                    boolean favorite = cursor.getInt(7) == 1;

                    fwList.add(new FreeWrite(id, date, title, genre, storyPic, filePath, duration, favorite));
                } while (cursor.moveToNext());
            }
        } catch(SQLiteException | ParseException | CursorIndexOutOfBoundsException e){
            Log.e("Database Error", e.getMessage());
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return fwList;
    }

    /**
     * Insert a free write record into the database.
     */
    public void insertFreeWrite(FreeWrite freeWrite) {
        ContentValues fwValues = new ContentValues();
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_DATE, freeWrite.getDateAsString());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_TITLE, freeWrite.getTitle());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_GENRE, freeWrite.getGenre());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_STORY_PICS, freeWrite.getStoryPics());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_FILEPATH, freeWrite.getFilepath());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_DURATION, freeWrite.getDuration());
        fwValues.put(FreeWriteDatabaseHelper.FreewriteTable.COL_FAV, freeWrite.isFavorite());

        db.insert(FreeWriteDatabaseHelper.FreewriteTable.TABLE, null, fwValues);
    }

    /**
     * Update the favorite column of a free write.
     * @param freeWrite - the FfeeWrite that will be updated.
     */
    public void updateFreeWriteFavorite(FreeWrite freeWrite) {

        ContentValues values = new ContentValues();
        values.put(FreeWriteDatabaseHelper.FreewriteTable.COL_FAV, freeWrite.isFavorite());

        db.update(FreeWriteDatabaseHelper.FreewriteTable.TABLE, values,
                FreeWriteDatabaseHelper.FreewriteTable.COL_ID + " = ?",
                new String[] {Integer.toString(freeWrite.getId())});
    }

    /**
     * The freewrite to delete.
     * @param freeWrite - the freeWrite that will be deleted.
     * @return true if the row was deleted successfully.
     */
    public boolean deleteFreeWrite(FreeWrite freeWrite) {
        int rowsDeleted = db.delete(FreeWriteDatabaseHelper.FreewriteTable.TABLE,
                        FreeWriteDatabaseHelper.FreewriteTable.COL_ID + " = ?",
                                    new String[] {Integer.toString(freeWrite.getId())});

        return rowsDeleted > 0;
    }

}
