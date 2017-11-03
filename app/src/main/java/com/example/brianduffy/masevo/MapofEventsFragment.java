package com.example.brianduffy.masevo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Date;
import java.util.ArrayList;

public class MapofEventsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnCameraMoveListener {
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 45;

    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location loc;
    private User user;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
    ArrayList<Event> testList = new ArrayList<>();
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private SettingsClient mSettingsClient;



    public static MapofEventsFragment newInstance() {
        MapofEventsFragment fragment = new MapofEventsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_map_of_events, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Get Location Manager and check for GPS & Network location services

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity()); // NULL pointer here maybe

        mSettingsClient = LocationServices.getSettingsClient(this.getActivity());

        createLocationRequest();

        createLocationCallback();
        buildLocationSettingsRequest();

            startLocationUpdates();


    }


    public void markEvents(GoogleMap googleMap) {
        //TODO create a list of events from database and add markers to those locations
        for (int i = 0; i < MainActivity.user.nearby.size(); i++) {
            LatLng eventloc = new LatLng(MainActivity.user.nearby.get(i).location.latitude,
                    MainActivity.user.nearby.get(i).location.longitude);
            googleMap.addMarker(new MarkerOptions().position(eventloc)
                    .title(MainActivity.user.nearby.get(i).eventName));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                        startLocationUpdates();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

//
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(10000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        //mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void startLocationUpdates() throws SecurityException{
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

                       // updateUI();
                    }
                })
                .addOnFailureListener(this.getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
//                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                // Log.e(TAG, errorMessage);
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                       // updateUI();
                       // startUI();
                    }
                });
    }
    private void startUI() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            onLocationChanged(location);
                            startLocationUpdates();

                            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                        }

                    }
                });
    }


    private void updateUI() {

        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            onLocationChanged(location);
                            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                onLocationChanged(mCurrentLocation);
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        markEvents(mMap);
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling  **************************** NEED TO ACCOUNT FOR THIS ***************************
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            onLocationChanged(location);
                            startLocationUpdates();

                            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                        }
                        if (location == null) {
                            onMapReady(googleMap);
                        }
                    }
                });


        // mMap.setOnCameraMoveListener(this);
        //  onLocationChanged(mFusedLocationClient.getLastLocation().getResult());

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        // locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onCameraMove() {

    }
}
