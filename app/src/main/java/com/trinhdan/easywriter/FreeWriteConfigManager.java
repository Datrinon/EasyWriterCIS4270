package com.trinhdan.easywriter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

/*
    Model to manage the state of the free writing exercise.
 */
public class FreeWriteConfigManager {

    public static final String DEBUG_TAG = "CONFIG MANAGER";
    public static final int BASE_MINUTES = 5;
    public static final int DIE_SIDES = 116; // the font-awesome package came with 116 images.

    private static int[] dicePictureIds;
    private static FreeWriteConfigManager gameManager;

    private long timerDuration;
    private long targetTime;
    private boolean timerRunning;
    private long timeLeft;
    private long durationMilliseconds;

    private int diceQuantity;

    private String chosenGenre;

    private ArrayList<Integer> chosenDieFaceList;

    public static FreeWriteConfigManager getInstance(){
        if (gameManager == null) {
            gameManager = new FreeWriteConfigManager(); // .
        }

        return gameManager;
    }

    private FreeWriteConfigManager(){
        timerDuration = 0;
        diceQuantity = 0;
        timerRunning = false;
    }

    // region Timer Related Operations
    // Starting timer after resuming activity
    public void startTimer(long millisLeft) {
        durationMilliseconds = millisLeft;
        timerDuration = SystemClock.uptimeMillis() + durationMilliseconds;
        timerRunning = true;
    }

    public void startTimer(){
        // Add 1 sec to duration so timer stays on current second longer
        durationMilliseconds = (timerDuration * 60 + 1) * 1000;
        targetTime = SystemClock.uptimeMillis() + durationMilliseconds;
        timerRunning = true;
    }

    public void stopTimer() {
        timerRunning = false;
    }

    public void pauseTimer() {
        timeLeft = targetTime - SystemClock.uptimeMillis();
        timerRunning = false;
    }

    public void resumeTimer() {
        targetTime = SystemClock.uptimeMillis() + timeLeft;
        timerRunning = true;
    }

    public long getRemainingMilliseconds() {
        if (timerRunning) {
            return Math.max(0, targetTime - SystemClock.uptimeMillis());
        }
        return 0;
    }

    public int getRemainingSeconds() {
        if (timerRunning) {
            return (int) ((getRemainingMilliseconds() / 1000) % 60);
        }
        return 0;
    }

    public int getRemainingMinutes() {
        if (timerRunning) {
            return (int) (((getRemainingMilliseconds() / 1000) / 60) % 60);
        }
        return 0;
    }

    public long getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(long timerDuration) {
        this.timerDuration = timerDuration;
    }


    public int getProgressPercent() {
        if (durationMilliseconds != 1000) {
            return Math.min(100, 100 - (int) ((getRemainingMilliseconds() - 1000) * 100 /
                    (durationMilliseconds - 1000)));
        }
        return 0;
    }

    public String timerToString() {
        return String.format(Locale.getDefault(), "%02d:%02d",
                getRemainingMinutes(), getRemainingSeconds());
    }

    //endregion

    //region Dice Related Operations
    public static int[] getAllDicePictureIDs(Context context) {
        if (dicePictureIds == null) {
            dicePictureIds = new int[DIE_SIDES];
            for (int i = 0; i < dicePictureIds.length; i++) {
                dicePictureIds[i] = context.getResources().getIdentifier("pic_" + i,
                        "drawable", "com.trinhdan.easywriter");
            }
        }
        return dicePictureIds;
    }
    public int getDiceQuantity() {
        return diceQuantity;
    }

    public void setDiceQuantity(int diceQuantity) {
        this.diceQuantity = diceQuantity;
    }

    public ArrayList<Integer> getChosenDieFaceList() {
        return chosenDieFaceList;
    }

    public void setChosenDieFaceList(ArrayList<Integer> chosenDieFaceList) {
        this.chosenDieFaceList = chosenDieFaceList;
    }
}
