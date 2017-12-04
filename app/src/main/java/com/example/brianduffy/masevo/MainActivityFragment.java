package com.example.brianduffy.masevo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.example.brianduffy.masevo.MainActivity.mGeofenceRequestIntent;
import static com.example.brianduffy.masevo.MainActivity.mGoogleApiClient;
//import static com.example.brianduffy.masevo.R.id.thing_proto;

/**
 * Created by Brian on 9/18/2017.
 */

public class MainActivityFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
   TextView priv_text;
   EditText priv_edit;
   Button priv_button;
   Switch eventSwitch;
   boolean eventType;
   TextView eventTypeText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view

        return inflater.inflate(R.layout.join_private_event,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

       priv_text = getView().findViewById(R.id.text_priv_id);
        priv_edit = getView().findViewById(R.id.priv_id);
        priv_button = getView().findViewById(R.id.join_p);
        eventTypeText = getView().findViewById(R.id.puborprivtext);
        eventSwitch = getView().findViewById(R.id.eventtype);

        eventSwitch.setOnCheckedChangeListener(this);


        priv_button.setOnClickListener(this);

    }



    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.join_p:
                //TODO verify valid id

                if (isValidID(priv_edit.getText().toString())) {

                    //TODO TODO fsaldfjweoigjwe;gji *********************************
                    ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(Integer.parseInt(priv_edit.getText().toString()),
                            MainActivity.user.emailAddress,!eventType);
                    Thread thread = new Thread(threadJoinEvent);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Pair<Boolean,Integer> ret = threadJoinEvent.getReturnResult();


                        if (ret.second != 0) {
                            Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                                    .getErrorMessage(ret.second),Toast.LENGTH_LONG).show();
                        } else {
                        Event event;
                        ThreadGetEvents getEvents = new ThreadGetEvents();
                        Thread t = new Thread(getEvents);
                        t.start();
                            try {

                                t.join();
                                Pair<ArrayList<? extends Event>,Integer> ret1 = getEvents.getReturnResult();

                                if (ret1.second != 0 ) {
                                    Toast.makeText(getContext(),Error.getErrorMessage(ret1.second),Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    for (Event e : ret1.first) {
                                        if (e.eventID == Integer.parseInt(priv_edit.getText().toString())) {
                                            event = e;

                                            MainActivity.mGeofenceList.add(new Geofence.Builder()
                                                    // Set the request ID of the geofence. This is a string to identify this
                                                    // geofence.
                                                    .setRequestId(event.eventName)

                                                    // Set the circular region of this geofence.
                                                    .setCircularRegion(
                                                            event.location.latitude,
                                                            event.location.longitude,
                                                            event.radius
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
                                            //TODO comment out for espresso testing
                                            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                                                    MainActivity.getGeofencingRequest(), mGeofenceRequestIntent);

                                        }
                                    }
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //TODO implement success
                            FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            MyEventsFragment myEventsFragment = new MyEventsFragment();
                            ft.replace(R.id.content_frame, myEventsFragment);
                            ft.addToBackStack(null);
                            ft.commit();


                        }




                } else {
                    priv_text.setText("Error: You entered an invalid ID!");

                }
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.eventtype:
                if (isChecked) {

                    eventTypeText.setText("Private Event");
                    eventType = false;
                } else {

                    eventTypeText.setText("Public Event");
                    eventType = true;
                }

        }

    }


    public boolean isValidID(String id) {
        int lim = 19;
        //TODO check upperbounds of id String
        if (id.length() == 0) {
            Toast.makeText(getContext(),"You have not entered an ID!",Toast.LENGTH_SHORT).show();
        }
        if (id.startsWith("-") && id.length() <= lim+1) { // negetive id
            return true;
        } else if (!id.startsWith("-") && id.length() < lim) {
            return true;
        } else {
            Toast.makeText(getContext(),"ID is too long!",Toast.LENGTH_SHORT).show();

            return false;
        }

   }

}