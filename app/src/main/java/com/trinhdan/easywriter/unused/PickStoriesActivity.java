package com.trinhdan.easywriter.unused;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trinhdan.easywriter.models.FreeWriteConfigManager;
import com.trinhdan.easywriter.R;

import java.util.ArrayList;

public class PickStoriesActivity extends AppCompatActivity {

    ViewGroup diceContainer;
    ImageView[] dice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_stories);

        // assign to variable to make it easier to read
        int chosenDiceAmt = FreeWriteConfigManager.getInstance().getDiceQuantity();

        Log.e("Test", "Game Manager Says: " + FreeWriteConfigManager.getInstance().getDiceQuantity());

        // TODO: Implement this on a background thread
        // Create an ArrayList that will hold all references to every picture that a die can have.
        ArrayList<Integer> dicePictureIds = new ArrayList<>();
        for(int i = 1; i <= FreeWriteConfigManager.DIE_SIDES; i++){
            dicePictureIds.add(getResources().getIdentifier("pic_"+i,
                                            "drawable", "com.trinhdan.easywriter"));
        }

        dice = new ImageView[chosenDiceAmt];

        diceContainer = findViewById(R.id.dice_container);

        for (int i = 0; i < chosenDiceAmt; i++){
            dice[i] = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400, 1);

            dice[i].setLayoutParams(layoutParams);
            dice[i].setPadding(0, 15, 15, 0);
            dice[i].setImageResource(R.mipmap.question_box);

            diceContainer.addView(dice[i]);

        }

    }
}