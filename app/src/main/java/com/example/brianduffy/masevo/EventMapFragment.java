package com.example.brianduffy.masevo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.*;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Brian Duffy on 10/26/2017.
 */

public class EventMapFragment extends Fragment implements OnMapReadyCallback, SwipeRefreshLayout.OnRefreshListener{
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 45;
    Event event;
    private FusedLocationProviderClient mFusedLocationClient;
    ListView userList;
    private GoogleMap mMap;
    ArrayList<User> activeUsers;
    ArrayList<Permission> perms = new ArrayList<>();
    private LocationManager locationManager;
    private android.location.Location loc;
    private Geofence geofence;
    ArrayList<String> userlisting = new ArrayList<>();
    private GeofencingRequest mGeofenceRequest;
    private String GeoReqID = "myEvent";
    SwipeRefreshLayout swipe;
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
        userList = view.findViewById(R.id.userview);
        //userList.setOnItemClickListener(this);
        showUsers(event.eventUsers.userPerm); // list view
        swipe = view.findViewById(R.id.swiperef);
        swipe.setOnRefreshListener(this);

        //updateList(event.eventUsers.userPerm);

        // May not need...........
        for (Map.Entry<String,Permission> entry: event.eventUsers.userPerm.entrySet()) {
            userlisting.add(entry.getKey());
            perms.add(entry.getValue());

        }
        registerForContextMenu(userList);


        return view;
    }
    public void showUsers(Map<String, Permission> active) {
        UserListAdapter adapter = new UserListAdapter(active);
        userList.setAdapter(adapter);

    }
    private void updateList(Map<String,Permission> permUser) {
        //TODO maybe need to delete the old UserListAdapter
        UserListAdapter adapter = new UserListAdapter(permUser);
        userList.setAdapter(adapter);
        swipe.setRefreshing(false);


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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.userview) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.event_menu, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Permission perm;
        switch(item.getItemId()) {

            case R.id.ban:
                perm = event.eventUsers.userPerm.get(MainActivity.user.emailAddress);
                for (Map.Entry<String,Permission> entry: event.eventUsers.userPerm.entrySet()) {
                    if (entry.getKey().equals(userList.getItemAtPosition((info).position))) {
                        if (perm.getPermissionLevel() > entry.getValue().getPermissionLevel()) {
                            //TODO remove the user from the event



                            //TODO server call update the event users
                            // maybe let user know they have been kicked

                            break;
                        }
                    }
                }
                // TODO do nothing, maybe send a toast message error


                return true;
            case R.id.kick:




                    // TODO do nothing, maybe send a toast message error



                return true;
            case R.id.priv:
                /*TODO get privilege level sorted out ie can host set user to host?
                also if creator sets thier perm level to a lower value, maybe find first user
                who has host permissions. If none, then set a user to creator or delete the event...
                */




                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // get the lat and lon from the serialized data sent from MainActivity and set locaiton to that
        // this data is the location of the event that they clicked from the listview fragments
        // fragment
        LatLng eventloc = new LatLng(event.location.latitude, event.location.longitude);
        mMap.addCircle(new CircleOptions()
                .center(eventloc)
                .radius(event.radius)
                .strokeColor(Color.BLUE));
        mMap.addMarker(new MarkerOptions().position(eventloc).title(event.eventName));

        //displayUsers();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(eventloc, 15);
        mMap.moveCamera(cameraUpdate);


    }
    //TODO server call Implement this for real
    public Location getUserLoc(String email) {

        //TODO update lat and lon to users location
        float lat = 0.0f;
        float lon = 0.0f;


        return new Location(lat,lon);
    }
    // TODO get users from a specific event via server call.
    // TODO build new users with only coordinates and the user email address.
    // may not need to build list. just keep using event.eventUsers.active HashMap
    public void displayUsers() {

        for (Map.Entry<String,Boolean> entry: event.eventUsers.userActive.entrySet()) {

            // if the user is active, meaning they are in radius of event
            if (entry.getValue()) {
                Location userLoc = getUserLoc(entry.getKey());

                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(userLoc.latitude,userLoc.longitude))
                        .radius(2)
                        .fillColor(Color.GREEN)
                        .strokeColor(Color.GREEN));

            }
            // TODO  the users location can be their last known location in the database

        }

    }

    @Override
    public void onRefresh() {

        //TODO remove the old user dots and put new ones there ******************

        // on swipe refresh, display the users on map
        displayUsers();
        swipe.setEnabled(false);

    }


}


