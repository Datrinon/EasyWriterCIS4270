package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startGame;
    private Button viewHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get buttons and register click listeners to them.
        startGame = findViewById(R.id.main_StartGame);
        viewHistory = findViewById(R.id.main_ViewHistory);

        // Start game should... start a new game.
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // View history should... take you to an activity that allows one to view the previous freewrites.
        viewHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreeWriteListActivity.class);
                startActivity(intent);
            }
        });

    }
}