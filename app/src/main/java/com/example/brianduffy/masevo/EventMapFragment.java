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
import android.util.Pair;
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


import com.example.chambe41.masevo.ThreadBanUser;
import com.example.chambe41.masevo.ThreadCreateEvent;
import com.example.chambe41.masevo.ThreadGetActiveLoc;
import com.example.chambe41.masevo.ThreadGetUserEvents;
import com.example.chambe41.masevo.ThreadMakeHost;
import com.example.chambe41.masevo.ThreadMakeOwner;
import com.example.chambe41.masevo.ThreadMakeUser;
import com.example.chambe41.masevo.ThreadRemoveUser;
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
    ArrayList<Location> userlocs = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    private LocationManager locationManager;
    private android.location.Location loc;
    private Geofence geofence;
    ArrayList<String> userlisting = new ArrayList<>();
    private GeofencingRequest mGeofenceRequest;
    private String GeoReqID = "myEvent";
    SwipeRefreshLayout swipe;
    int pos = 0;
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
         pos = 0;
        for (int i = 0; i < MainActivity.user.myevents.size(); i++) {
            if (event.eventID == MainActivity.user.myevents.get(i).eventID) {
                pos = i;
                break;
            }
        }


        Users eventUsers = MainActivity.eventusers.get(pos);
        for (Map.Entry<String, PermissionActivePair> entry : eventUsers.userPerm.entrySet()) {
            userlisting.add(entry.getKey());
        }
        userList.setAdapter(new UserListAdapter(this.getContext(),userlisting));
        swipe = view.findViewById(R.id.swiperef);
        swipe.setOnRefreshListener(this);



        registerForContextMenu(userList);


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
        Boolean isPub = false;
        if (event instanceof PublicEvent) {
            isPub = true;
        }
        switch(item.getItemId()) {

            case R.id.ban:
               ThreadBanUser banUser = new ThreadBanUser(event.eventID,MainActivity.user.emailAddress
                       , users.get((info).position).emailAddress,isPub);
               Thread thread = new Thread(banUser);
               thread.start();
                try {
                    thread.join();

                    Pair<Boolean,Integer> ret1 =banUser.getReturnResult();
                    if (ret1.second != 0) {
                        Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                                .getErrorMessage(ret1.second),Toast.LENGTH_SHORT).show();
                        return false;

                    } else {
                       //TODO do stuff

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.kick:
                ThreadRemoveUser removeUser = new ThreadRemoveUser(event.eventID,MainActivity.user.emailAddress,
                        users.get((info).position).emailAddress,isPub);
                Thread t3 = new Thread(removeUser);
                t3.start();
                try {
                    t3.join();
                    Pair<Boolean,Integer> ret1 =removeUser.getReturnResult();
                    if (ret1.second != 0) {
                        Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                                .getErrorMessage(ret1.second),Toast.LENGTH_SHORT).show();
                        return false;

                    } else {
                        //TODO do stuff
                        MainActivity.eventusers.get(pos).
                                userPerm.remove(users.get((info).position).emailAddress);
                        userlisting.remove((info).position);



                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                return true;
            case R.id.makeuser:

                ThreadMakeUser makeUser = new ThreadMakeUser(event.eventID,MainActivity.user.emailAddress,
                        users.get((info).position).emailAddress,isPub);
                Thread thread1 = new Thread(makeUser);
                thread1.start();
                try {
                    thread1.join();
                    Pair<Boolean,Integer> ret = makeUser.getReturnResult();

                    if (ret.second != 0) {
                        Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Success!",Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.makehost:
                ThreadMakeHost makeHost = new ThreadMakeHost(event.eventID,MainActivity.user.emailAddress,
                        users.get((info).position).emailAddress,isPub);
                Thread thread2 = new Thread(makeHost);
                thread2.start();
                try {
                    thread2.join();
                    Pair<Boolean,Integer> ret = makeHost.getReturnResult();

                    if (ret.second != 0) {
                        Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Success!",Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.makeowner:
                ThreadMakeOwner makeOwner = new ThreadMakeOwner(event.eventID,MainActivity.user.emailAddress,
                        users.get((info).position).emailAddress,isPub);
                Thread thread3 = new Thread(makeOwner);
                thread3.start();
                try {
                    thread3.join();
                    Pair<Boolean,Integer> ret = makeOwner.getReturnResult();

                    if (ret.second != 0) {
                        Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Success!",Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng eventloc = new LatLng(event.location.latitude, event.location.longitude);

        displayUsers();
        //TODO travers

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(eventloc, 15);
        mMap.moveCamera(cameraUpdate);


    }

    // TODO get users from a specific event via server call.
    // TODO build new users with only coordinates and the user email address.
    // may not need to build list. just keep using event.eventUsers.active HashMap
    public void displayUsers() {

        Boolean isPub = false;
        if (event instanceof PublicEvent) {
            isPub = true;
        }
        ThreadGetActiveLoc getActiveLoc = new ThreadGetActiveLoc(event.eventID,MainActivity.user.emailAddress,isPub);

        Thread thread = new Thread(getActiveLoc);
        thread.start();
        try {
            thread.join();
            Pair<ArrayList<Location>,Integer> ret = getActiveLoc.getReturnResult();

            if (ret.second != 0) {
                Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();
            } else {
                userlocs.clear();
                userlocs.addAll(ret.first);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMap.clear();
        LatLng eventloc = new LatLng(event.location.latitude, event.location.longitude);
        mMap.addCircle(new CircleOptions()
                .center(eventloc)
                .radius(event.radius)
                .strokeColor(Color.BLUE));
        mMap.addMarker(new MarkerOptions().position(eventloc).title(event.eventName));

        for (Location loc: userlocs) {
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(loc.latitude,loc.longitude))
                    .radius(2)
                    .strokeColor(Color.GREEN));
        }


    }

    @Override
    public void onRefresh() {

        //TODO remove the old user dots and put new ones there ******************

        // on swipe refresh, display the users on map
        displayUsers();

        ThreadGetUserEvents getUserEvents = new ThreadGetUserEvents(MainActivity.user.emailAddress);
        Thread thread = new Thread(getUserEvents);
        thread.start();
        try {
            thread.join();
            Pair<ArrayList<Event>,ArrayList<Users>> ret = getUserEvents.getReturnResult();
            if (ret == null) {
                Toast.makeText(getContext(),"Unable to connect to server!",Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.eventusers = ret.second;
                MainActivity.user.myevents = ret.first;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        userList.setAdapter(new UserListAdapter(this.getContext(),userlisting));

        swipe.setEnabled(false);

    }


}


