package com.park.allparking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Stack;

/**
 * An activity representing a single ParkingDetail detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ParkingDetailListActivity}.
 */
public class ParkingDetailActivity extends AppCompatActivity {

    private static final String LOG_PARKING_DETAIL_ACTIVITY = ParkingDetailActivity.class.getSimpleName();

    public static Stack<Class<?>> parents = new Stack<Class<?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_PARKING_DETAIL_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        super.onCreate(savedInstanceState);
        parents.push(getClass());
        setContentView(R.layout.activity_parking_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ParkingDetailFragment.ARG_ITEM_ID, String.valueOf(getIntent().getSerializableExtra("parkingID")));
            arguments.putString(ParkingDetailFragment.ARG_ITEM_TITLE, String.valueOf(getIntent().getSerializableExtra("parkingTitle")));
            ParkingDetailFragment fragment = new ParkingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.parking_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_PARKING_DETAIL_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent parentActivityIntent = new Intent(this, parents.pop());
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            navigateUpTo(parentActivityIntent);
            return true;
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
        }
        return super.onOptionsItemSelected(item);
    }
}
