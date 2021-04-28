package com.trinhdan.easywriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

// Follow fragment code for a master - detail sort of ordeal.
public class HistoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        FragmentManager fm = getSupportFragmentManager();
        // FragmentTransaction is not necessary, we can just chain add and commit to it later.
        // Call findFragment on the activity's viewgroup id rather than the fragment itself b/c
        // the fragment is only materialized within the container.
        Fragment fragment = fm.findFragmentById(R.id.list_fragment_container);

        if (fragment == null){
            fragment = new HistoryListFragment();
            // add to the listfragmentcontainer a historylistfragment, and then commit() to materialize.
            fm.beginTransaction().add(R.id.list_fragment_container, fragment).commit();
        }




    }
}