package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class PickStoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_stories);

        Log.e("Test", "Game Manager Says: " + FreeWriteConfigManager.getInstance().getDiceQuantity());

        ArrayList<Integer> picArr = new ArrayList<>();


        for(int i = 1; i <= 116; i++){
            picArr.add(getResources().getIdentifier("pic_"+i,
                                            "drawable", "com.trinhdan.easywriter"));
        }

        Log.d("Pause", "Dab");

    }
}