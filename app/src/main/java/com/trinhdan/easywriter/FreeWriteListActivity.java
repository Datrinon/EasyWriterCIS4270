package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

// Follow fragment code for a master - detail sort of ordeal.
public class FreeWriteListActivity extends AppCompatActivity implements FreeWriteListFragment.OnFreeWriteSelectedListener {

    private static final String KEY_FREEWRITE_ID = "FreeWriteID";
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
     * Observer pattern decouples logic to launch the DetailActivity from the DetailFragment.
     */
    @Override
    public void onFreeWriteSelected(int fwID) {

        freeWriteID = fwID;

        // Send the freewrite ID of the clicked button to DetailsActivity
        Intent intent = new Intent(this, FreeWriteDetailsActivity.class);
        intent.putExtra(FreeWriteDetailsActivity.EXTRA_FREEWRITE_ID, freeWriteID);
        startActivity(intent);
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
}