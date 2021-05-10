package com.trinhdan.easywriter.controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.trinhdan.easywriter.R;


public class FreeWriteListActivity extends AppCompatActivity implements FreeWriteListFragment.OnFreeWriteSelectedListener {

    private static final String KEY_FREEWRITE_ID = "FreeWriteID";
    private final int REQUEST_CODE_DETAIL = 0;
    private int freeWriteID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freewrite_list);

        FragmentManager fm = getSupportFragmentManager();
        // FragmentTransaction is not necessary, we can just chain add and commit to it later.
        // Call findFragment on the activity's viewgroup id rather than the fragment itself b/c
        // the fragment is only materialized within the container.
        Fragment fragment = fm.findFragmentById(R.id.list_fragment_container);

        if (fragment == null){
            fragment = new FreeWriteListFragment();
            // add to the listfragmentcontainer a historylistfragment, and then commit() to materialize.
            fm.beginTransaction().add(R.id.list_fragment_container, fragment).commit();
        }
    }

    /**
     * Callback when a list entry is clicked on -- see the fragment for more details.
     * Utilizes Observer pattern to observe its fragment.
     */
    @Override
    public void onFreeWriteSelected(int fwID) {

        freeWriteID = fwID;

        // Send the freewrite ID of the clicked button to DetailsActivity
        Intent intent = new Intent(this, FreeWriteDetailsActivity.class);
        intent.putExtra(FreeWriteDetailsActivity.EXTRA_FREEWRITE_ID, freeWriteID);
        startActivityForResult(intent, REQUEST_CODE_DETAIL);
    }

    // This method is for later when we implement tablet view.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save state when something is selected
        if (freeWriteID != -1) {
            savedInstanceState.putInt(KEY_FREEWRITE_ID, freeWriteID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            int deleteId = data.getIntExtra(FreeWriteDetailsFragment.EXTRA_FREEWRITE_ID, -1);

            Bundle bundle = new Bundle();
            bundle.putInt("DeleteID", deleteId);

            getSupportFragmentManager().findFragmentById(R.id.list_fragment_container).setArguments(bundle);

        }
    }
}