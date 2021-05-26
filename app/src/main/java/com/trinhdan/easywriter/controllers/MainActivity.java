package com.trinhdan.easywriter.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trinhdan.easywriter.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final Random rng = new Random();
    private final AnimatorSet captionTitleIdleAnimation = new AnimatorSet();

    private Button startGame;
    private Button viewHistory;
    private TextView captionTextView;
    private String[] captions;
    private int currentCaptionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Easy Writers");

        // set caption to a random string resource from the array.
        captionTextView = findViewById(R.id.title_caption);
        captions = getResources().getStringArray(R.array.title_captions);
        currentCaptionIndex = rng.nextInt(captions.length);
        captionTextView.setText(captions[currentCaptionIndex]);

        // Get buttons and register click listeners to them.
        startGame = findViewById(R.id.main_StartGame);
        viewHistory = findViewById(R.id.main_ViewHistory);

        // start a new game.
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreeWriteConfigActivity.class);
                startActivity(intent);
            }
        });

        // View history
        viewHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreeWriteListActivity.class);
                startActivity(intent);
            }
        });

        // just for fun, a way to change the caption.
        captionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTitleCaption();
            }
        });

        animateTitleCaption();
    }

    /**
     * Animates the title caption upon launch; expands and shrinks the caption repeatedly.
     */
    private void animateTitleCaption() {
        ObjectAnimator enlargenX = ObjectAnimator.ofFloat(captionTextView, "scaleX", 1.10f);
        enlargenX.setDuration(900);
        enlargenX.setRepeatMode(ValueAnimator.REVERSE);
        enlargenX.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator enlargenY = ObjectAnimator.ofFloat(captionTextView, "scaleY", 1.10f);
        enlargenY.setDuration(900);
        enlargenY.setRepeatMode(ValueAnimator.REVERSE);
        enlargenY.setRepeatCount(ValueAnimator.INFINITE);

        captionTitleIdleAnimation.play(enlargenX).with(enlargenY);
        captionTitleIdleAnimation.start();


    }

    /**
     * Upon the user pressing the caption, this method is part of a callback that swaps the title caption
     * for another caption.
     */
    private void swapTitleCaption(){
        // pause the animation
        captionTitleIdleAnimation.pause();

        // Show the caption for 5 seconds, slide it off screen. Change the text. Show the new one.
        int screenWidth = this.getWindow().getDecorView().getWidth();
        ObjectAnimator moveTextOff = ObjectAnimator.ofFloat(captionTextView, "translationX", screenWidth);
        moveTextOff.setDuration(500);
        moveTextOff.start();

        moveTextOff.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int previousCaptionIndex = currentCaptionIndex;
                do {
                    currentCaptionIndex = rng.nextInt(captions.length);
                } while (previousCaptionIndex == currentCaptionIndex); // ensures a new quote each time.

                captionTextView.setText(captions[currentCaptionIndex]);

                // Animate from above the screen down to default location
                ObjectAnimator moveBoardOn = ObjectAnimator.ofFloat(captionTextView,
                        "translationX", -screenWidth, 0);
                moveBoardOn.setDuration(700);
                moveBoardOn.start();
            }
        });

        captionTitleIdleAnimation.resume();
    }
}