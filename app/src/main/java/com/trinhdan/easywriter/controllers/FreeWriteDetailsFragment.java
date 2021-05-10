package com.trinhdan.easywriter.controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trinhdan.easywriter.models.FreeWrite;
import com.trinhdan.easywriter.database.FreeWriteDAO;
import com.trinhdan.easywriter.R;
import com.trinhdan.easywriter.Utility;

import java.util.Objects;

public class FreeWriteDetailsFragment extends Fragment {

    public static final String EXTRA_FREEWRITE_ID = "com.trinhdan.easywriter.ID";
    private static String KEY_FREEWRITE_ID= "freeWriteID";

    Button returnButton;
    TextView title;
    Button favoriteButton;
    Button deleteButton;
    TextView dateTextView;
    TextView durationTextView;
    TextView genreTextView;
    ViewGroup diceContainer;
    TextView storyTextView;


    private FreeWrite freeWrite;

    public FreeWriteDetailsFragment(){
        // required empty constructor to use newInstance to transmit data from other activities.
    }

    public static Fragment newInstance(int fwID) {
        FreeWriteDetailsFragment fragment = new FreeWriteDetailsFragment(); // create new object of this class so that it remember the arguments later
        Bundle args = new Bundle();
        args.putInt(KEY_FREEWRITE_ID, fwID);
        fragment.setArguments(args); // set arguments
        return fragment; // return fragment

        // A few lines later in the detail activity, when the fragment is instantiated,
        // we use getArguments to get the id from it.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int fwID = (getArguments() != null) ? getArguments().getInt(KEY_FREEWRITE_ID) : 1;
        freeWrite = FreeWriteDAO.getInstance(getContext()).fetchFreeWriteByID(fwID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_write_details, container, false);

        returnButton = view.findViewById(R.id.exit_detail_button);
        title = view.findViewById(R.id.detail_story_name);
        favoriteButton = view.findViewById(R.id.favorite_button);
        deleteButton = view.findViewById(R.id.recycle_button);
        dateTextView = view.findViewById(R.id.timestamp_textview);
        durationTextView = view.findViewById(R.id.duration_textview);
        genreTextView = view.findViewById(R.id.genre_textview);
        diceContainer = view.findViewById(R.id.story_dice_container_detailview);
        storyTextView = view.findViewById(R.id.story_textview);

        // Give the return option the ability to finish the activity.
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        // Set the title to the title of the story.
        title.setText(freeWrite.getTitle());

        // Set the favorite button.
        inflateFavoriteButton();

        // Delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this free write? This cannot be undone.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO (Complete) Delete the record in the database associated with this and close the activity; update the arraylist too.
                        // Steps
                        // 1. Call delete method to remove from the database
                        // 2. If successful, finish the detail activity and send the result to the list activity
                        // 3. In OnActivityCompleted(), The list activity sets the argument on the fragment
                        // 4. The fragment receives the argument as a bundle and deletes the right entry
                        // 5. The fragment updates the list by resetting the adapter
                        // 6. If the list is actually empty after the delete, then we generate a empty screen view.
                        boolean deleteSuccessful = FreeWriteDAO.getInstance(getContext()).deleteFreeWrite(freeWrite);
                        //boolean deleteSuccessful = true; // debug
                        if (deleteSuccessful) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_FREEWRITE_ID, freeWrite.getId());
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Deletion failed!", Toast.LENGTH_SHORT).show();
                        }

                        //finish(); // return to summary view.
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
        });

        // Date
        dateTextView.setText(freeWrite.getDateForDisplay());

        // durationTextView
        long minutes = freeWrite.getDuration() / 60;
        long seconds = freeWrite.getDuration() % 60;
        durationTextView.setText(String.format(getString(R.string.detail_duration_string),
                                 minutes, seconds));

        // genre
        genreTextView.setText(freeWrite.getGenre());

        // story dice
        diceContainer.removeAllViews(); // remove the template views.
        // use substring to cut off last , which otherwise would lead to an empty string in the array.
        loadStoryDicePics();

        // story textview
        storyTextView.setText(Utility.readFromFile(getContext(), freeWrite));

        return view;
    }

    private void inflateFavoriteButton() {
        Drawable notFavoriteImg = getContext().getDrawable(R.drawable.favorite_line);

        Drawable favoriteImg = getContext().getDrawable(R.drawable.favorite_fill);
        favoriteImg.setTint(getContext().getResources().getColor(R.color.orange_500));

        if (freeWrite.isFavorite()){
            favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, favoriteImg,null,null);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteButton.getCompoundDrawables()[1] == favoriteImg) {
                    favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, notFavoriteImg, null, null);

                    Toast.makeText(getContext(), "Unfavorited.", Toast.LENGTH_SHORT).show();
                    freeWrite.setFavorite(false);
                } else {
                    favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, favoriteImg, null, null);
                    Toast.makeText(getContext(), "Favorited!", Toast.LENGTH_SHORT).show();
                    freeWrite.setFavorite(true);
                }

                // Update database
                FreeWriteDAO.getInstance(getContext()).updateFreeWriteFavorite(freeWrite);
            }
        });
    }

    private void loadStoryDicePics() {
        String[] picIds = freeWrite.getStoryPics().split(",");

        // Configuration information for each pic in the for loop.
        // set width to 32dp
        int dpWidth = (int) Utility.convertDpToPixel(32, getContext());
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(dpWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // set margins to 5dp
        int dpMargin = (int)Utility.convertDpToPixel(5, getContext());
        layoutParams.setMargins(dpMargin, 0, dpMargin, 0);

        for (String picId: picIds) {
            // Set the image resource.
            ImageView pic = new ImageView(getContext());
            pic.setImageResource(getContext().
                                 getResources().
                                 getIdentifier(picId, "drawable",
                                     "com.trinhdan.easywriter"));

            pic.setLayoutParams(layoutParams);
            diceContainer.addView(pic);
        }
    }

}