package com.trinhdan.easywriter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This activity allows the user to manage a freewrite session before they begin it.
 * They can select the duration of the session and the number of free-writing dice to apply.
 */
public class FreeWriteConfigActivity extends AppCompatActivity {

    private FreeWriteConfigManager gameManager;
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
        gameManager = FreeWriteConfigManager.getInstance();

        // Get references to the views in the activity
        seekbar = findViewById(R.id.timer_bar);
        spinner = findViewById(R.id.spinner_dice_quantity);
        timeChoiceDialog = findViewById(R.id.timer_setting_choice);
        diceQuantityDialog = findViewById(R.id.dice_setting_choice);
        launchSessionButton = findViewById(R.id.Begin_Session_Button);

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
                gameManager.setDiceQuantity(position + 1);
                diceQuantityDialog.setText(String.format(getString(R.string.dice_choice_dialog), position + 1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(2); // set default die to three.
        //endregion

        // Seekbar configuration.
        seekbar = findViewById(R.id.timer_bar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress + FreeWriteConfigManager.BASE_MINUTES;
                gameManager.setTimerDuration(minutes);
                timeChoiceDialog.setText(String.format(getString(R.string.timer_choice_dialog), minutes));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // In this section, we set the dialog and have the game manager track the user's selection.
        int defaultMinutes = seekbar.getProgress() + FreeWriteConfigManager.BASE_MINUTES;
        int defaultDice = spinner.getSelectedItemPosition() + 1;

        // Set the dialog of the options.
        timeChoiceDialog.setText(String.format(getString(R.string.timer_choice_dialog),
                defaultMinutes));
        diceQuantityDialog.setText(String.format(getString(R.string.dice_choice_dialog), defaultDice));

        gameManager.setDiceQuantity(defaultDice);
        gameManager.setTimerDuration(defaultMinutes);

        // The button at the bottom will launch the game session.
        launchSessionButton.setOnClickListener(v -> {
            Intent intent = new Intent(FreeWriteConfigActivity.this, FreeWriteActivity.class);
            startActivity(intent);
            //TODO: Use startActivityForResult to boot user to main menu if they abort by finishing this
        });
    }

}