package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class FreeWriteDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_FREEWRITE_ID = "freeWriteID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write_details);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.details_fragment_container);

        if (fragment == null) {
            int fwID = getIntent().getIntExtra(EXTRA_FREEWRITE_ID, 1);
            fragment = FreeWriteDetailsFragment.newInstance(fwID);
            fragmentManager.beginTransaction()
                    .add(R.id.details_fragment_container, fragment)
                    .commit();
        }
    }

}