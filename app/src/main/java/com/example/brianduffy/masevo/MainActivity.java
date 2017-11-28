package com.example.brianduffy.masevo;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
//TODO *********************************************************************************************
/*TODO
    need to implement arraylist of geofences for every event. Upon entry of the current user,
    add the current user to the arraylist of the event and update the event arraylist
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "MainActivity";
    static GoogleApiClient mGoogleApiClient;
    static final int REQUEST_LOCATION = 45;
    ArrayList<Event> events = new ArrayList<>();
    MapofEventsFragment mapevents;
    LocationManager lm;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    static User user;
    static android.location.Location location;

    //Buttons
    final String save_loc = "save.txt";
    String text = "";

    static PendingIntent mGeofenceRequestIntent;


    public static ArrayList<Geofence> mGeofenceList;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(LoginActivity.emailAddress, 0.0f, 0.0f, new ArrayList<Integer>(), new ArrayList<Integer>());
        //TODO determine what to do with this. Are we doing it or not? read in from file my events id's
        File file = new File(getFilesDir(), save_loc);
        try {
            BufferedReader fr = new BufferedReader(new FileReader(file));
            text = fr.readLine();
            String[] g = text.split(",");
            for (String aG : g) {
                user.myIDs.add(Integer.parseInt(aG));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        user.events = new PublicEvent().getEvents();

        //TODO get data from databse call to populate events variable

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapevents = MapofEventsFragment.newInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // show the create event fragment on start up
        CreateEventFragment createEventFragment = new CreateEventFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, createEventFragment)
                .addToBackStack(null)
                .commit();

        // check those permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // get their location based on ip address
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        user.myevents.add(new PublicEvent("name","desc",new Date(1000000),new Date(100000000),
//                40.4257f,-86.92446f,100f,user.emailAddress));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
        mGeofenceList = new ArrayList<>();
        populateGeofenceList();

        // initilize location services to get users location over time
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // NULL pointer here maybe

        // used to check if the user has locations services on or off
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationRequest();

        createLocationCallback();
        buildLocationSettingsRequest();

        startLocationUpdates();

    }


    private void createLocationRequest() {

        // init location request
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(1000 * 60 * 2); // five minutes

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        //mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    public void onLocationChanged(Location location) {

        //************************** DEBUG *****************************
        Toast.makeText(this,"Lat: " + location.getLatitude() +
                " Lon: " + location.getLongitude(),Toast.LENGTH_SHORT).show();
        // user has moved, get new lat and lon and move camera to that location
       //TODO server call to update user's location



    }
    private void buildLocationSettingsRequest() {

        // build location settings dialog for permission handleing
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() throws SecurityException {

        // start loc updates iff their location settings are turned on.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        // The user's location services are turned on. we are good to go!

                        // start the looper for getting location every time interval defined in
                        // createLocationRequest()
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
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
                                    rae.startResolutionForResult(MainActivity.this,REQUEST_CHECK_SETTINGS);

                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }



    private PendingIntent getGeofenceTransitionPendingIntent() {

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public static GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle connectionHint) {
        // Get the PendingIntent for the geofence monitoring request.
        // Send a request to add the current geofences.
        startLocationUpdates();

        //TODO comment out when doing espresso testing
        if (mGeofenceList.size() == 0) {
            mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();

            return;
        }
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();

        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,getGeofencingRequest(),mGeofenceRequestIntent);

        Toast.makeText(this, "geofence has begun!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGeofenceRequestIntent) {
            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, mGeofenceRequestIntent);
        }
    }

    private void populateGeofenceList() {

        for (Event e: user.myevents) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(e.eventName)

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            e.location.latitude,
                            e.location.longitude,
                            e.radius
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    // Create the geofence.
                    .build());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION){
            mapevents.onActivityResult(requestCode, resultCode, data);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onBackPressed() {
        // when drawer open this closes it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out_button) { // user chose the signout button
            signOut();
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        // signout the user from the app
        final Intent login = new Intent(this, LoginActivity.class);
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        startActivity(login);
                        // [END_EXCLUDE]
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        populateGeofenceList();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) { // home icon will likely be create event now



            // AS of now the create event fragment is the home fragment
            CreateEventFragment createEventFragment = new CreateEventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, createEventFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.mapofevents) {

            // start the map of events fragment
            MapofEventsFragment mapofEventsFragment = new MapofEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mapofEventsFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.feedback) {
            FeedbackFragment feedbackFragment = new FeedbackFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, feedbackFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_manage) {
            // TODO either remove or add something here

        } else if (id == R.id.myevents) {
            // start the my events listview fragment
            MyEventsFragment myEventsFragment = new MyEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, myEventsFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nearbyevents) {
            // start the nearby events fragment
            NearbyEventsFragment nearbyEventsFragment = new NearbyEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nearbyEventsFragment)
                    .addToBackStack(null)
                    .commit();
        }

        // close drawer on item selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    @Override
    public void onClick(View view) {
//
    }

    @Override
    protected void onPause() { // TODO FIGURE out what we are doing with file io
        super.onPause();
        stopLocationUpdates();

        File f = new File(getFilesDir(), save_loc);
        try {
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i < user.myevents.size(); i++) {
                pw.write(user.myevents.get(i).eventID + ",");
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }
}
