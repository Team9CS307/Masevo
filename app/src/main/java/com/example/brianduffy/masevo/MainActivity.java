package com.example.brianduffy.masevo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    private static final String TAG = "MainActivity";
    GoogleApiClient mGoogleApiClient;
    static final int REQUEST_LOCATION= 45;
    ArrayList<Event> events = new ArrayList<>();
    MapofEventsFragment mapevents;
//    TextView textView;
//    EditText one;
    static User user;
    //Buttons

//    ArrayList<String> list = new ArrayList<>();
//    Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User("emailAddress",0.0f,0.0f,new HashSet<Integer>(),new HashSet<Integer>());

        float lat = 0f;
        float lon = 1f;
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 1","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 2","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 3","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 4","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 5","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 6","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 7","event description",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        user.events.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));

        user.nearby.add(new PublicEvent(new Date(3),new Date(500),40.426829f, -86.911904f,
                "Public Event 1","event description",500,"String email"));
        user.nearby.add(new PublicEvent(new Date(3),new Date(500),40.426208f, -86.915455f,
                "Public Event 2","event description",500,"String email"));
        user.nearby.add(new PublicEvent(new Date(3),new Date(500),40.428782f,-86.913517f,
                "Public Event 3","event description",500,"String email"));
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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MainActivityFragment myEventsFragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, myEventsFragment)
                .addToBackStack(null)
                .commit();
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
        super.onStart();

    }

    @Override
    public void onBackPressed() {
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
        if (id == R.id.sign_out_button) {
            signOut();
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
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

        if (id == R.id.home) {

            MainActivityFragment mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mainActivityFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.mapofevents) {

            MapofEventsFragment mapofEventsFragment = new MapofEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mapofEventsFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.myevents) {

            MyEventsFragment myEventsFragment = new MyEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, myEventsFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nearbyevents) {

            NearbyEventsFragment nearbyEventsFragment = new NearbyEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nearbyEventsFragment)
                    .addToBackStack(null)
                    .commit();
        }

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

}
