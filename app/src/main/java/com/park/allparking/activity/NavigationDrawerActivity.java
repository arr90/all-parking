package com.park.allparking.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.park.allparking.vo.Parking;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_NAVIGATION_DRAWER_ACTIVITY = NavigationDrawerActivity.class.getSimpleName();

    private static final boolean LOCATION_IN_REAL_TIME = false;

    private GoogleMap mMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    public static Stack<Class<?>> parents = new Stack<Class<?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMap();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this, "nav_camera", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "nav_gallery", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "nav_slideshow", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "nav_manage", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "nav_share", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "nav_send", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        mMap = googleMap;
        mMap = loadLocations();

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, true ? R.raw.style_map_dark : R.raw.style_map_light));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
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
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                try {
                    Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this);
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
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

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
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                if (marker != null) {
                    marker.remove();
                }
                Toast.makeText(NavigationDrawerActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                setMarker("Marked now", latLng);
//                Toast.makeText(MapsActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

                Intent intent = new Intent(NavigationDrawerActivity.this, ParkingDetailActivity.class);
                intent.putExtra(ParkingDetailFragment.ARG_ITEM_ID, marker.getSnippet());
                intent.putExtra(ParkingDetailFragment.ARG_ITEM_TITLE, marker.getTitle());
                startActivity(intent);
            }
        });

        mMap.setMyLocationEnabled(true);
    }

    private void initMap() {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private GoogleMap loadLocations() {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

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
    private void setMarker(String locality, LatLng latLng) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

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
    private void goToLocation(LatLng latLng) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(cameraUpdate);
    }
    private void goToLocationZoom(LatLng latLng, float zoom) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_NAVIGATION_DRAWER_ACTIVITY, "LOG [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] {**********}");

        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        goToLocationZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15);

        if (LOCATION_IN_REAL_TIME) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(2000);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}