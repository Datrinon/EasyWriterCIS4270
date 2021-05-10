package com.trinhdan.easywriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class FreeWriteOverActivity extends AppCompatActivity {

    EditText givenTitle;
    CheckBox markedFavorite;
    Button discardButton;;
    Button saveButton;
    ImageView starGFX;
    FreeWriteConfigManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write_over);

        givenTitle = findViewById(R.id.title_field);
        markedFavorite = findViewById(R.id.fav_checkbox);
        discardButton = findViewById(R.id.discard_button);
        saveButton = findViewById(R.id.save_button);
        starGFX = findViewById(R.id.star_success_icon);
        manager = FreeWriteConfigManager.getInstance();

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDiscardMessage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTitleFieldNotEmpty()){
                    findViewById(R.id.no_title_error_message).setVisibility(View.VISIBLE);
                } else {
                    // Save free write to file
                    String filename = Utility.saveToFile(getApplicationContext(), manager.getUserFreeWrite());
                    manager.getUserFreeWrite().setTitle(givenTitle.getText().toString());
                    manager.getUserFreeWrite().setFilepath(filename);
                    manager.getUserFreeWrite().setFavorite(markedFavorite.isChecked());
                    // 1: Insert record into database.
                    FreeWriteDAO.getInstance(getApplicationContext())
                            .insertFreeWrite(manager.getUserFreeWrite());
                    // 2: Inform user of successful save with dialog
                    OKDialogFragment dialog = new OKDialogFragment();
                    dialog.show(getSupportFragmentManager(), "SuccessDialog");
                    // 3: User presses OK on dialog to go back to main activity screen.
                    // ... this occurs in the dialog.
                    // 4: Lastly, terminate the session.
                    manager.terminateManager();
                }
            }
        });

        // play the star animation.
        animateStar();
    }

    private void displayDiscardMessage() {
        // Supposedly hides the keyboard. Let's see
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

        AlertDialog.Builder builder = new AlertDialog.Builder(FreeWriteOverActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Are you really sure you want to discard this free write?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manager.terminateManager();
                // exit if yes
                //finish();
                // Instead of using finish(), use setFlags() in order to relaunch MainActivity and then clear the backstack.
                // See more at: https://developer.android.com/guide/components/activities/tasks-and-back-stack
                Intent intent = new Intent(FreeWriteOverActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkTitleFieldNotEmpty() {
        return (givenTitle.getText().length() > 0);
    }

    @Override
    public void onBackPressed() {
        displayDiscardMessage();
    }

    private void animateStar(){
        // show the star
        starGFX.setVisibility(View.VISIBLE);

        ObjectAnimator fadeInStar = ObjectAnimator.ofFloat(starGFX, "alpha", 0, 1.0f);
        fadeInStar.setDuration(500);

        ObjectAnimator enlargenStarX = ObjectAnimator.ofFloat(starGFX, "scaleX", 1.15f);
        enlargenStarX.setDuration(800);

        ObjectAnimator enlargenStarY = ObjectAnimator.ofFloat(starGFX, "scaleY", 1.15f);
        enlargenStarY.setDuration(800);

        ObjectAnimator scaleStarX = ObjectAnimator.ofFloat(starGFX, "scaleX", 1.0f);
        scaleStarX.setDuration(300);

        ObjectAnimator scaleStarY = ObjectAnimator.ofFloat(starGFX, "scaleY", 1.0f);
        scaleStarY.setDuration(300);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(fadeInStar).with(enlargenStarX).with(enlargenStarY);
        animSet.play(scaleStarX).with(scaleStarY).after(enlargenStarY);
        animSet.start();
    }

    public static class OKDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Save Successful!");
            builder.setMessage("Freewrite successfully saved. Press OK to return to main menu.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(),
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            return builder.create();
        }
    }


}