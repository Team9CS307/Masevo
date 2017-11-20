package com.example.brianduffy.masevo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
//TODO *********************************************************************************************
/*TODO
    need to implement arraylist of geofences for every event. Upon entry of the current user,
    add the current user to the arraylist of the event and update the event arraylist
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    GoogleApiClient mGoogleApiClient;
    static final int REQUEST_LOCATION = 45;
    ArrayList<Event> events = new ArrayList<>();
    MapofEventsFragment mapevents;
    LocationManager lm;
    //    TextView textView;
//    EditText one;
    static User user;
    static android.location.Location location;

    //Buttons
    final String save_loc = "save.txt";
    String text = "";

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
//        MainActivityFragment mainActivityFragment = new MainActivityFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, mainActivityFragment)
//                .addToBackStack(null)
//                .commit();

        // show the create event fragment on start up
        CreateEventFragment createEventFragment = new CreateEventFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, createEventFragment)
                .addToBackStack(null)
                .commit();

        // check those permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        // get their location based on ip address
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) { // home icon will likely be create event now

//            MainActivityFragment mainActivityFragment = new MainActivityFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.content_frame, mainActivityFragment)
//                    .addToBackStack(null)
//                    .commit();

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

        } else if (id == R.id.nav_slideshow) {
            // TODO either remove or add something here

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
            // start the nearby events fragment // TODO need to add functionality to this
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

    }
}
