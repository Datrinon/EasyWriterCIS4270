package com.trinhdan.easywriter.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.trinhdan.easywriter.models.FreeWriteConfigManager;
import com.trinhdan.easywriter.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This activity allows the user to manage a freewrite session before they begin it.
 * They can select the duration of the session and the number of free-writing dice to apply.
 */
public class FreeWriteConfigActivity extends AppCompatActivity {

    private FreeWriteConfigManager manager;
    private TextView timeChoiceDialog;
    private TextView diceQuantityDialog;
    private Spinner spinner;
    private SeekBar seekbar;
    private Button launchSessionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write_config);

        // Let game refer to a session of free writing.
        // Spawn instance of the game manager, this will keep track of game settings and the timer.
        manager = FreeWriteConfigManager.getInstance();

        // Get references to the views in the activity
        seekbar = findViewById(R.id.timer_bar);
        spinner = findViewById(R.id.spinner_dice_quantity);
        timeChoiceDialog = findViewById(R.id.timer_setting_choice);
        diceQuantityDialog = findViewById(R.id.dice_setting_choice);
        launchSessionButton = findViewById(R.id.Begin_Session_Button);

        configureSeekbar();
        configureDiceSpinner();
        configureGenreSpinner();

        // Get the default value for the seekbar and spinner, use that to customize dialog.
        int defaultMinutes = seekbar.getProgress() + FreeWriteConfigManager.BASE_MINUTES;
        int defaultDice = spinner.getSelectedItemPosition() + 1;

        // Set the dialog of the options.
        timeChoiceDialog.setText(String.format(getString(R.string.timer_choice_dialog),
                defaultMinutes));
        diceQuantityDialog.setText(String.format(getString(R.string.dice_choice_dialog), defaultDice));

        manager.setDiceQuantity(defaultDice);
        manager.setTimerDuration(defaultMinutes);

        // The button at the bottom will launch the writing session.
        launchSessionButton.setOnClickListener(v -> {
            pickDieFaces();
            Intent intent = new Intent(FreeWriteConfigActivity.this, FreeWriteActivity.class);
            startActivity(intent);
        });
    }

    private void configureGenreSpinner() {
        // region Spinner Initialization for user to select genre.
        spinner = findViewById(R.id.spinner_genre_choice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genre,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                switch(position){
                    case 0: // Randomizer
                        selectRandomGenre();
                        break;
                    default: //
                        manager.setChosenGenre(item);

                }

            }

            private void selectRandomGenre() {
                List<String> genreList = Arrays.asList(getResources().getStringArray(R.array.genre));
                Random rng = new Random();
                // The first category is random, so we should remove that from rng.
                int selection = rng.nextInt(genreList.size()-1) + 1;
                manager.setChosenGenre(genreList.get(selection));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion
    }

    private void configureDiceSpinner() {
        // region Spinner Initialization for user to select dice.
        spinner = findViewById(R.id.spinner_dice_quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dice_number,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manager.setDiceQuantity(position + 1);
                diceQuantityDialog.setText(String.format(getString(R.string.dice_choice_dialog), position + 1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(2); // set default die to three.
        //endregion
    }
    private void configureSeekbar() {
        // Seekbar configuration.
        seekbar = findViewById(R.id.timer_bar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress + FreeWriteConfigManager.BASE_MINUTES;
                manager.setTimerDuration(minutes);
                timeChoiceDialog.setText(String.format(getString(R.string.timer_choice_dialog), minutes));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void pickDieFaces() {
        int[] dicePicArr = FreeWriteConfigManager.getAllDicePictureIDs(this);
        manager.setChosenDieFaceList(new ArrayList<>()); // Wipe arrayList
        Random rng = new Random();
        for (int i = 0; i < manager.getDiceQuantity(); i++){
            // pick a random num in between 0 and the DIE_SIDES
            int picId = rng.nextInt(FreeWriteConfigManager.DIE_SIDES);

            manager.getChosenDieFaceList().add(dicePicArr[picId]);
            Log.d("ChosenDicePictureArray", String.valueOf(dicePicArr[picId]));
        }

    }
}