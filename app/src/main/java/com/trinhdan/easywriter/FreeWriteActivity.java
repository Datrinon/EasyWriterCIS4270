package com.trinhdan.easywriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class FreeWriteActivity extends AppCompatActivity {

    private ViewGroup diceContainer;
    private EditText writing;
    private TextView timer;
    private TextView genre;
    private Handler timerHandler;
    private FreeWriteConfigManager manager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write);

        manager = FreeWriteConfigManager.getInstance();
        timer = findViewById(R.id.timer);
        genre = findViewById(R.id.genre);
        writing = findViewById(R.id.freewrite_area);
        diceContainer = findViewById(R.id.story_dice_container);

        // Disable scrolling in the outer/parent view when focused on the TextArea
        // See more at: https://learnpainless.com/android/how-to-create-textarea-in-android/
        writing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        displayDiceImages();
        //manager.startTimer();
        //TODO test functionality of timer with an X second override.
        manager.startTimer(35000);
        genre.setText(manager.getChosenGenre());
        timerHandler = new Handler(Looper.getMainLooper());
        timerHandler.post(updateTimerRunnable);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to quit? The free write session will not be saved.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // exit if yes
                //finish();
                // Instead of using finish(), use setFlags() in order to relaunch MainActivity and then clear the backstack.
                // See more at: https://developer.android.com/guide/components/activities/tasks-and-back-stack
                Intent intent = new Intent(FreeWriteActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void timerCompleted() {
        manager.stopTimer();
        // Remove any remaining Runnables that may reside in UI message queue
        timerHandler.removeCallbacks(updateTimerRunnable);
        // Inform the user that the game is over
        // Call function to end the session.
        endSession();
    }

    private void displayTimeWarningMessage(){
        Log.d(Utility.DEBUG_TAG, "Executing warning message...");
        Toast.makeText(this, "Time is running low. Start wrapping up your work.", Toast.LENGTH_LONG).show();
        timer.setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    private final Runnable updateTimerRunnable = new Runnable() {
        boolean firedWarningMessage = false;

        @Override
        public void run() {

            // Update UI to show remaining time and progress
            timer.setText(manager.timerToString());
            int progress = manager.getProgressPercent();
            // Only post Runnable if more time remains
            if (progress == 100) { // if the timer is complete then end the timer
                timerCompleted();
            }
            else {
                if (manager.getRemainingSeconds() == 30 && firedWarningMessage != true){ // When the timer has 30 seconds left, warn the user.
                    firedWarningMessage = true;
                    displayTimeWarningMessage();
                }
                timerHandler.postDelayed(this, 200);
                Log.d(Utility.DEBUG_TAG, String.format("Completion: %d%%, Seconds: %d", progress, manager.getRemainingSeconds()));
            }
        }
    };

    private void displayDiceImages(){
        diceContainer.removeAllViews(); // Remove template views.

        int dpWidth = (int)Utility.convertDpToPixel(32, this);
        int dpHeight = (int)Utility.convertDpToPixel(35, this);
        int dpSpacing = (int)Utility.convertDpToPixel(7, this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidth, dpHeight, 1);

        layoutParams.setMargins(dpSpacing, 0, dpSpacing, 0);

        for (int i = 0; i < manager.getChosenDieFaceList().size(); i++) {
            ImageView pic = new ImageView(this);

            pic.setLayoutParams(layoutParams);
            pic.setPadding(dpSpacing, 0, 0, 0);
            pic.setImageResource(manager.getChosenDieFaceList().get(i));

            diceContainer.addView(pic);
        }
    }

    /**
     * Assigned action for the finish button. Opens a dialog which calls endSession().
     * @param view
     */
    public void showDialogEndGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Finished with the free write?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                endSession();
                // exit if yes
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    // save the details to a FreeWrite object
    // call an intent to start a new activity
    private void endSession() {
        //TODO either choose to save the getText to a string (in-memory) or perform a save and delete operation.
        String textEntry = writing.getText().toString();
        String storyPicString = "";
        // To save storyPicString in the SQLite database we describe as a comma separated value
        // Later to get the icons the user had we can split the string and then use getIdentifier()
        for (Integer picId: manager.getChosenDieFaceList()){
            storyPicString += getResources().getResourceEntryName(picId) + ",";
        }

        //TODO SESSION OVER ANIMATION... GAME OVER POPUP
        FreeWrite fw = new FreeWrite(-1, new Date(), "Untitled",
                                    manager.getChosenGenre(), storyPicString, "filepath_here",
                                    manager.getTimeElapsedInSeconds(), false);

        fw.setTextEntry(textEntry);

        Log.d(Utility.DEBUG_TAG, fw.getDateAsString());
        Log.d(Utility.DEBUG_TAG, fw.getGenre());
        Log.d(Utility.DEBUG_TAG, String.valueOf(fw.getDuration()));
        Log.d(Utility.DEBUG_TAG, storyPicString);
        Log.d(Utility.DEBUG_TAG, fw.getTextEntry());

        manager.setUserFreeWrite(fw);

        Intent intent = new Intent(FreeWriteActivity.this, FreeWriteOverActivity.class);

        startActivity(intent);
    }

}