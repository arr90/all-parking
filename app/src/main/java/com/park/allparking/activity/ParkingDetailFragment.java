package com.park.allparking.activity;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.park.allparking.business.ParkingBusiness;
import com.park.allparking.vo.Parking;

import java.text.SimpleDateFormat;

/**
 * A fragment representing a single ParkingDetail detail screen.
 * This fragment is either contained in a {@link ParkingDetailListActivity}
 * in two-pane mode (on tablets) or a {@link ParkingDetailActivity}
 * on handsets.
 */
public class ParkingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "parking_id";
    public static final String ARG_ITEM_TITLE = "parking_title";

    private Parking mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ParkingDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getActivity().getIntent().hasExtra(ARG_ITEM_ID)){
            String parkingID = this.getActivity().getIntent().getExtras().get(ARG_ITEM_ID).toString();
            mItem = ParkingBusiness.getInstance(this.getActivity()).getParkingByID(parkingID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.parking_detail, container, false);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.parking_detail_create_date)).setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(mItem.getCreateDate()));
            ((TextView) rootView.findViewById(R.id.parking_detail_description)).setText(mItem.getDescription());
            ((TextView) rootView.findViewById(R.id.parking_detail_latlng)).setText(mItem.getLatLng().toString());
            ((TextView) rootView.findViewById(R.id.parking_detail_capacity)).setText(mItem.getCapacity().toString());
            ((TextView) rootView.findViewById(R.id.parking_detail_price)).setText(mItem.getPrice().toString());
            ((TextView) rootView.findViewById(R.id.parking_detail_start_time)).setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(mItem.getStartTime()));
            ((TextView) rootView.findViewById(R.id.parking_detail_end_time)).setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(mItem.getEndTime()));
        }
        return rootView;
    }
}
