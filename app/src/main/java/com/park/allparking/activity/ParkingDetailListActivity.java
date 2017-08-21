package com.park.allparking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.park.allparking.business.ParkingBusiness;
import com.park.allparking.vo.Parking;

import java.util.List;
import java.util.Stack;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of ParkingDetails. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ParkingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ParkingDetailListActivity extends AppCompatActivity {

    private static final String LOG_PARKING_DETAIL_LIST_ACTIVITY = ParkingDetailListActivity.class.getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static Stack<Class<?>> parents = new Stack<Class<?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_PARKING_DETAIL_LIST_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        super.onCreate(savedInstanceState);
        parents.push(getClass());
        setContentView(R.layout.activity_parking_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.parking_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.parking_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//TODO        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
        String arg = getIntent().getExtras().getString("searchText");
        arg = this.getIntent().getStringExtra("searchText");
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ParkingBusiness.getInstance(this).findParkingByTitle(arg)));
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Parking> mValues;

        public SimpleItemRecyclerViewAdapter(List<Parking> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTitle());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ParkingDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                        ParkingDetailFragment fragment = new ParkingDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.parking_detail_container, fragment).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ParkingDetailActivity.class);
                        intent.putExtra(ParkingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        intent.putExtra(ParkingDetailFragment.ARG_ITEM_TITLE, holder.mItem.getTitle());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Parking mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}