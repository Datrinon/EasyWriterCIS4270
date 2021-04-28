package com.trinhdan.easywriter;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;

public class HistoryListFragment extends Fragment {

    HistoryDatabase dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout of this fragment
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        LinearLayout layout = (LinearLayout) view;

        // Get the database (helper) -- suppose it provides us a connection string to it.
        dbHelper = new HistoryDatabase(getContext()); // getContext is a fragment method that allows us to get the activity's context. Cool.

        getFreewritesByDate();

        // Create the buttons using the band names and ids from BandDatabase
        List<Band> bandList = BandDatabase.getInstance(getContext()).getBands();
        for (int i = 0; i < bandList.size(); i++) {
            Button button = new Button(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 10);   // 10 px
            button.setLayoutParams(layoutParams);

            // Set the text to the band's name and tag to the band ID
            Band band = BandDatabase.getInstance(getContext()).getBand(i + 1);
            button.setText(band.getName());
            button.setTag(Integer.toString(band.getId()));

            // All the buttons have the same click listener
            button.setOnClickListener(buttonClickListener);

            // Add the button to the LinearLayout
            layout.addView(button);
        }

        return view;
    }

//    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // Start DetailsActivity
//            Intent intent = new Intent(getActivity(), DetailsActivity.class);
//            startActivity(intent);
//        }
//    };

    // Get all the freewrites sorted by the most recent date.
    private void getFreewritesByDate(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + HistoryDatabase.FreewriteTable.TABLE + " ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        try {
            /*
                COL_ID
                COL_DATE
                COL_TITLE
                COL_LINK
                COL_DURATION
                COL_FAV
             */
            do {
                long id = cursor.getLong(0);
                Date date =  cursor.getString(1);
                String title = cursor.getString(2);
                String link = cursor.getString(3);
                double duration = cursor.getFloat(4);
                boolean favorite = cursor.getInt(5) == 1 ? true : false;

            } while (cursor.moveToNext());
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}