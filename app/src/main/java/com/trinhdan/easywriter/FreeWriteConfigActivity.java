package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * This activity allows the user to manage a freewrite session before they begin it.
 * They can select the duration of the session and the number of free-writing dice to apply.
 */
public class FreeWriteConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write_config);

        // Add in to initialize spinner
    }
}