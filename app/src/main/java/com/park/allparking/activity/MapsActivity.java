package com.park.allparking.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.park.allparking.business.ParkingBusiness;
import com.park.allparking.dao.ParkingDAO;
import com.park.allparking.vo.Parking;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final boolean LOCATION_IN_REAL_TIME = false;
    private static final String LOG_MAPS_ACTIVITY = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initMap();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        mMap = googleMap;
        mMap = loadLocations();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null) {
                    goToLocationZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                try {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    LatLng latLng = marker.getPosition();
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    Address address = addressList.get(0);
                    marker.setTitle(address.getLocality());
                    marker.showInfoWindow();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                View view = getLayoutInflater().inflate(R.layout.info_window, null);

                TextView tvLocality = (TextView) view.findViewById(R.id.tv_locality);
                TextView tvLat = (TextView) view.findViewById(R.id.tv_lat);
                TextView tvLng = (TextView) view.findViewById(R.id.tv_lng);
                TextView tvSnippet = (TextView) view.findViewById(R.id.tv_snippet);

                LatLng latLng = marker.getPosition();
                System.out.println(latLng.toString());

                tvLocality.setText("Title: " + marker.getTitle() + " / ID: " + marker.getId());
                tvLat.setText("Latitude:  " + latLng.latitude);
                tvLng.setText("Longitude: " + latLng.longitude);
                tvSnippet.setText(marker.getSnippet());

                tvSnippet.setText(marker.getId());
                return view;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                if (marker != null) {
                    marker.remove();
                }
                Toast.makeText(MapsActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                setMarker("Marked now", latLng);
//                Toast.makeText(MapsActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                Intent intent = new Intent(MapsActivity.this, ParkingDetailActivity.class);
                intent.putExtra(ParkingDetailFragment.ARG_ITEM_ID, marker.getSnippet());
                intent.putExtra(ParkingDetailFragment.ARG_ITEM_TITLE, marker.getTitle());
                startActivity(intent);
            }
        });

        mMap.setMyLocationEnabled(true);
    }

    public void changeStyleMap(View v) {
        //TODO changeStyleMap
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, true ? R.raw.style_map_dark : R.raw.style_map_light);
        mMap.setMapStyle(style);
    }

    public void getDetail(View v) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        Intent intent = new Intent(this, ParkingDetailListActivity.class);
        startActivity(intent);
        Toast.makeText(this, Thread.currentThread().getStackTrace()[2].getMethodName(), Toast.LENGTH_SHORT).show();
    }

    private void goToMylocation() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

    }

    public void onMapSearch(View view) throws IOException {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

//        searchInMap();
        searchParkings();
    }

    private void searchInMap() throws IOException {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        EditText locationSearch = (EditText) findViewById(R.id.searchText);
        String location = locationSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocationName(location, 1);
        Address address = addressList.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        goToLocationZoom(latLng, 15);

        setMarker(locality, latLng);
    }

    private void searchParkings() throws IOException {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        EditText parkingSearch = (EditText) findViewById(R.id.searchText);
        String titleText = parkingSearch.getText().toString();

        Intent intent = new Intent(MapsActivity.this, ParkingDetailListActivity.class);
        intent.putExtra("searchText", titleText);
        startActivity(intent);
    }

    private void goToLocation(LatLng latLng) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(cameraUpdate);
    }

    private void goToLocationZoom(LatLng latLng, float zoom) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private void setMarker(String locality, LatLng latLng) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        if (marker != null) {
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.id.action_add_note))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(latLng)
                .snippet("I am Here");

        marker = mMap.addMarker(markerOptions);
    }

    private GoogleMap loadLocations() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        try {
            List<Parking> parkings = ParkingBusiness.getInstance(this).getAllParkings();

            for (Parking parking : parkings) {
                mMap.addMarker(
                        new MarkerOptions()
                                .position(parking.getLatLng())
                                .title(parking.getTitle())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .snippet(String.valueOf(parking.getId()))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMap;
    }

    private void initMap() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServiceAvailable() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onNormalMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSatelliteMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onTerrainMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void onHybridMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");


        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        goToLocationZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15);

        if (LOCATION_IN_REAL_TIME) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(2000);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+ Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+ Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+ Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        if(LOCATION_IN_REAL_TIME){
            if (location == null){
                Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
            } else{
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        }
    }

}