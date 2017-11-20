package com.example.brianduffy.masevo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Brian Duffy on 10/26/2017.
 */

public class EventMapFragment extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 45;
    Event event;
    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private android.location.Location loc;
    private User user;
    private Geofence geofence;
    private GeofencingRequest mGeofenceRequest;
    private String GeoReqID = "myEvent";
    public static EventMapFragment newInstance() {
        EventMapFragment fragment = new EventMapFragment();
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get data sent from previous activity
        Bundle bundle = getArguments();

        // rebuild serialized object with key event
        event= (Event) bundle.getSerializable("event");
        // expand view
        View view = inflater.inflate(R.layout.fragment_event_map, null, false);

        // get map call back from google api DO NOT CHANGE
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.eventmap);
        mapFragment.getMapAsync(this);


        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize location client for location services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity()); // NULL pointer here maybe


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode)
        {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // user does not have loc serv on
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();

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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // get the lat and lon from the serialized data sent from MainActivity and set locaiton to that
        // this data is the location of the event that they clicked from the listview fragments
        // fragment
        LatLng eventloc = new LatLng(event.location.latitude, event.location.longitude);

        googleMap.addMarker(new MarkerOptions().position(eventloc).title(event.eventName));



        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(eventloc, 15);
        googleMap.moveCamera(cameraUpdate);
    }
}


