package com.trinhdan.easywriter;

import android.content.Context;

/*
    Model to manage the state of the free writing exercise.
 */
public class FreeWriteConfigManager {

    public static final int BASE_MINUTES = 5;

    private static FreeWriteConfigManager gameManager;
    private double timerDuration;
    private int diceQuantity;

    public static FreeWriteConfigManager getInstance(){
        if (gameManager == null) {
            gameManager = new FreeWriteConfigManager(); // .
        }

        return gameManager;
    }

    private FreeWriteConfigManager(){
        timerDuration = 0;
        diceQuantity = 0;
    }

    public double getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(double timerDuration) {
        this.timerDuration = timerDuration;
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public void setDiceQuantity(int diceQuantity) {
        this.diceQuantity = diceQuantity;
    }
}
