package com.trinhdan.easywriter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FreeWriteActivity extends AppCompatActivity {

    
    private EditText writing;
    private TextView timer;
    private Handler timerHandler;
    private FreeWriteConfigManager manager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write);

        manager = FreeWriteConfigManager.getInstance(); // assign var manager to make it easier to type
        timer = findViewById(R.id.timer);
        writing = findViewById(R.id.freewrite_area);

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

        manager.startTimer();
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
                //if user pressed "yes", then he is allowed to exit from application
                finish();
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
    }

    private final Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {

            // Update UI to show remaining time and progress
            timer.setText(manager.timerToString());
            int progress = manager.getProgressPercent();
            // Only post Runnable if more time remains
            if (progress == 0) {
                timerCompleted();
            }
            else {
                timerHandler.postDelayed(this, 200);
            }
        }
    };
}