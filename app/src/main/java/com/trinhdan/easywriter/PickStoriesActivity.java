package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class PickStoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_stories);

        Log.e("Test", "Game Manager Says: " + FreeWriteConfigManager.getInstance().getDiceQuantity());
    }
}