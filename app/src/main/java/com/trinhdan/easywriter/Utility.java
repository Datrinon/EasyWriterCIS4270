package com.trinhdan.easywriter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.trinhdan.easywriter.models.FreeWrite;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;

/**
 * Utility provides a variety of methods that facilitate Free Write's operations, but don't really
 * belong to any class in specific.
 */
public class Utility {

    public static final String DEBUG_TAG = "Dan's Debug Logs";

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    //region IO related operations

    /**
     * Saves the freewrite to a text file.
     * Returns the filename.
     */
    public static String saveToFile(Context appContext, FreeWrite userFreeWrite) {
        String fileName = "freewrite_" + userFreeWrite.getDate().getTime() + ".txt"; // freewrite_{epoch}.txt. Guaranteed to always be unique.
        PrintWriter writer = null;
        try {
            FileOutputStream outputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new PrintWriter(outputStream);
            Scanner textStream = new Scanner(userFreeWrite.getTextEntry());
            Log.e(Utility.DEBUG_TAG, textStream.toString());
            String line;
            while (textStream.hasNextLine()){
                line = textStream.nextLine();
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            Log.e(Utility.DEBUG_TAG, "I/O ERROR: " + e.getMessage());
        } finally {
            writer.close();
        }

        return fileName;
    }

    /**
     * Reads from text file into memory. For use in detail view.
     * Adapted from Dr. Shi's TodoList example.
     */
    public static String readFromFile(Context appContext, FreeWrite userFreeWrite) {
        BufferedReader reader = null; // flush buffer before beginning.
        StringBuilder textEntry = new StringBuilder();

        try {
            // Read in list from internal file
            FileInputStream inputStream = appContext.openFileInput(userFreeWrite.getFilepath());
            reader = new BufferedReader(new InputStreamReader(inputStream));

            CharSequence line;
            while ((line = reader.readLine()) != null) {
                textEntry.append(line).append("\n");
            }
            userFreeWrite.setTextEntry(textEntry.toString());
        } catch (IOException ex) { // FileNotFoundException is a subclass of IOException.
            ex.printStackTrace();
        } finally {
            closeFileReader(reader);
        }

        return textEntry.toString();
    }

    /**
     * Close the file reader.
     * Adapted from Zybooks Java, Chapter 13.
     */
    private static void closeFileReader(Reader fileStream) {
        try {
            if (fileStream != null) {
                fileStream.close();
            }
        } catch (IOException e) {
            Log.e(Utility.DEBUG_TAG, "I/O ERROR: " + e.getMessage());
        }
    }
}

