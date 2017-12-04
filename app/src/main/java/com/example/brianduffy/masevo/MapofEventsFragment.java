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
import android.util.Pair;
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

        //instantiate a new Map of Events Fragment
        MapofEventsFragment fragment = new MapofEventsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // init a view and inflate it with the fragment map of events
        View view = inflater.inflate(R.layout.fragment_map_of_events, null, false);

        // create the google map view here
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Get Location Manager and check for GPS & Network location services
        ThreadGetEvents getEvents = new ThreadGetEvents();
        Thread t = new Thread(getEvents);
        t.start();
        try {
            t.join();
            Pair<ArrayList<? extends Event>,Integer> ret = getEvents.getReturnResult();

            if (ret.second != 0) {
                Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();

            }  else {
                testList.addAll(ret.first);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initilize location services to get users location over time
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity()); // NULL pointer here maybe

        // used to check if the user has locations services on or off
        mSettingsClient = LocationServices.getSettingsClient(this.getActivity());

        createLocationRequest();

        createLocationCallback();
        buildLocationSettingsRequest();

            startLocationUpdates();


    }


    public void markEvents(GoogleMap googleMap) {

        //TODO create a list of events from database and add markers to those locations
        // used to mark events from the nearby arraylist of the given user. This will be defined
        // populated in Oncreate of the MainActivity
        //TODO
        for (int i = 0; i < testList.size(); i++) {

            // only show public events that are nearby
            if (testList.get(i) instanceof PublicEvent) {

                // get the lat and lon
                LatLng eventloc = new LatLng(testList.get(i).location.latitude,
                        testList.get(i).location.longitude);

                // mark their location on the google map
                googleMap.addMarker(new MarkerOptions().position(eventloc)
                        .title(testList.get(i).eventName));
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode)
        {
                // case where user has location services turned off
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made, show toast message
                        Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();

                        //start the location updates
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

        // init location request
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(20000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        //mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void startLocationUpdates() throws SecurityException {

        // start loc updates iff their location settings are turned on.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        // The user's location services are turned on. we are good to go!

                        // start the looper for getting location every time interval defined in
                        // createLocationRequest()
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this.getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            // users loc services are off,
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // create resolution intent to allow the user to turn on loc services
                                    // in app
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }


    @Override
    public void onPause() {
        super.onPause();

        // when screen is put in background, stop location updates to reduce map api calls
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {

        // remove location updates. handled by google
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void createLocationCallback() {

        // When user reopens app this is started
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

        // build location settings dialog for permission handleing
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

        // init the google map enabling location zoom and go to my location
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // mark the events on the nearyby events arraylist
        markEvents(mMap);

        // This is here to make program compile. Already check for permissions above
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // get user's last location
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            // check if user has moved since last location.
                            onLocationChanged(location);

                            // got user's location, now start the location updates
                            startLocationUpdates();

                        }
                        if (location == null) {

                            // if location cannot be retrieved, continue to try and wait until they
                            // allow location services
                            onMapReady(googleMap);
                        }
                    }
                });

    }


    @Override
    public void onLocationChanged(Location location) {

        // user has moved, get new lat and lon and move camera to that location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

        // needed to compile. may handle this later
    }

    @Override
    public void onProviderEnabled(String s) {

        //if user has internet, try that location. Not implemented due to lack of precise location
        // needed to compile
    }

    @Override
    public void onProviderDisabled(String s) {

        //if user has internet, try that location. Not implemented due to lack of precise location
        // needed to compile
    }

    @Override
    public void onCameraMove() {

        //handle what happens when user moves map arround. not needed here
    }
}
