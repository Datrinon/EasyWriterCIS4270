package com.trinhdan.easywriter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/*
    Model to manage the state of the free writing exercise.
 */
public class FreeWriteConfigManager {
    // Constants

    public static final int BASE_MINUTES = 5;
    public static final int DIE_SIDES = 116; // the font-awesome package came with 116 images.
    // Class Fields
    private static int[] dicePictureIds;
    private static FreeWriteConfigManager gameManager;
    // Timer-related attributes
    private long timerDuration;
    private long targetTime;
    private boolean timerRunning;
    private long timeLeft;
    private long durationMilliseconds;
    // Dice-related attribute
    private ArrayList<Integer> chosenDieFaceList;
    private int diceQuantity;
    // Genre-related attribute
    private String chosenGenre;
    // Freewrite
    private FreeWrite userFreeWrite;

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
        targetTime = SystemClock.uptimeMillis() + durationMilliseconds;
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

    public long getTimeElapsedInSeconds(){
        // returns old system time
        return (SystemClock.uptimeMillis() - (targetTime - durationMilliseconds)) / 1000;
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
    //endregion

    //region Genre related operations
    public String getChosenGenre() {
        return chosenGenre;
    }

    public void setChosenGenre(String chosenGenre) {
        this.chosenGenre = chosenGenre;
    }

    //region FreeWrite related operations
    public FreeWrite getUserFreeWrite() {
        return userFreeWrite;
    }

    public void setUserFreeWrite(FreeWrite userFreeWrite) {
        this.userFreeWrite = userFreeWrite;
    }

}
