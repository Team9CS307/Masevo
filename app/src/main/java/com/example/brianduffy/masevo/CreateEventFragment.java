package com.example.brianduffy.masevo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.example.brianduffy.masevo.MainActivity.mGeofenceRequestIntent;
import static com.example.brianduffy.masevo.MainActivity.mGoogleApiClient;

/**
 * Created by Brian Duffy on 11/9/2017.
 */

public class CreateEventFragment extends android.support.v4.app.Fragment implements  View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{
    TextView title_text;
    EditText title;
    TextView desc_text;
    EditText desc;
    Button start_time;
    TextView start_text;
    TextView end_text;
    int PLACE_PICKER_REQUEST = 1;
    float latitude;
    float longitude;
    Switch eventSwitch;
    Button end_time;
    Button createButton;
    Button getLoc;
    Boolean eventType = true; // true is public, false is private
    TextView eventTypeText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view

        return inflater.inflate(R.layout.create_event_layout,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Initilize variables
        title_text = getView().findViewById(R.id.event_name_text);
        title = getView().findViewById(R.id.event_name);
        desc_text = getView().findViewById(R.id.event_desc_text);
        desc = getView().findViewById(R.id.event_desc);
        start_text = getView().findViewById(R.id.start_time);
        end_text = getView().findViewById(R.id.end_time);
        eventSwitch = getView().findViewById(R.id.event_type);
        start_time = getView().findViewById(R.id.start_date);
        end_time = getView().findViewById(R.id.end_date);
        getLoc = getView().findViewById(R.id.location_but);
        eventTypeText = getView().findViewById(R.id.event_type_text);
        createButton = getView().findViewById(R.id.create_event);

        // Set onclick listeners to buttons
        eventSwitch.setOnCheckedChangeListener(this);
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        getLoc.setOnClickListener(this);
        createButton.setOnClickListener(this);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) { // Response from google Place Picker Activity
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data); // Get place data

                latitude = (float) place.getLatLng().latitude;
                longitude = (float) place.getLatLng().longitude;
                String toastMsg = "latitude: " + latitude + " longitude: " + longitude; // Display

                makeText(this.getContext(), toastMsg, Toast.LENGTH_LONG).show();

            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_date: // startdate button id
                DialogFragment timestart = new TimePickerFragment();
                timestart.show(getActivity().getFragmentManager(),"timeSPicker"); // start Timepicker
                DialogFragment start = new DatePickerFragment();
                start.show(getActivity().getFragmentManager(), "startPicker"); // start datepicker

                break;
            case R.id.end_date: // enddate button id
                // the timepicker is started first because it is at the bottom of the view stack
                // then the datepicker is put on top. Once datepicker is done, timepicker moves to view.

                DialogFragment timeend = new TimePickerFragment();
                timeend.show(getActivity().getFragmentManager(),"timeEPicker"); //start timepicker
                DialogFragment end = new DatePickerFragment();
                end.show(getActivity().getFragmentManager(), "endPicker"); // start datepicker
                break;
            case R.id.location_but: // pick location button id

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.create_event: // create event button id
                //TODO add stuff here maybe
                String eventName = title.getText().toString();
                String eventDesc = desc.getText().toString();
                float radius;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");

                Date jud1 = null;
                Date jud2 = null;

                try {
                    jud1 = new Date(sdf.parse(start_text.getText().toString()).getTime());
                    jud2 = new Date(sdf.parse(end_text.getText().toString()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //TODO comment out when espresso testing
//                Calendar calendar = Calendar.getInstance();
//                java.util.Date curr = calendar.getTime();
//                if (!(curr.before(jud1) && curr.before(jud2) && jud1.before(jud2))) {
//                    Toast.makeText(getContext(),"Date is invalid!",Toast.LENGTH_LONG).show();
//                    break;
//                }
                int count = 0;
                if (validateEventName(eventName)) count++;
                if (validateEventDesc(eventDesc)) count++;
                if (validateDateText(start_text.getText().toString(),true)) count++;
                if (validateDateText(end_text.getText().toString(),false)) count++;
                if (validateLocation(new Location(latitude,longitude))) count++;
                else Toast.makeText(getContext(),"Please choose a location!",Toast.LENGTH_LONG).show();

                if (count != 5) {
                    Toast.makeText(getContext(),"Invalid input. Try again.",Toast.LENGTH_LONG).show();

                    break;
                }
                if (eventType) {
                         // todo change the radius or something
                    PublicEvent pubEvent = new PublicEvent(eventName,eventDesc,jud1,jud2,
                            latitude,longitude,100f,MainActivity.user.emailAddress);
                    pubEvent.eventUsers.userActive.put(MainActivity.user.emailAddress,true);
                    pubEvent.eventUsers.userPerm.put(MainActivity.user.emailAddress,
                            new Permission(2));
                    MainActivity.mGeofenceList.add(new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(pubEvent.eventName)

                            // Set the circular region of this geofence.
                            .setCircularRegion(
                                    pubEvent.location.latitude,
                                    pubEvent.location.longitude,
                                    pubEvent.radius
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
//                    LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
//                            MainActivity.getGeofencingRequest(),mGeofenceRequestIntent);


                    //TODO add to the maps in eventUsers Class
                    MainActivity.user.myevents.add(pubEvent);

                    FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    MyEventsFragment myEventsFragment = new MyEventsFragment();
                    ft.replace(R.id.content_frame, myEventsFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {

                    //TODO should private events have geofences?
                    PrivateEvent privEvent = new PrivateEvent(eventName,eventDesc,jud1,jud2,
                            latitude,longitude,100f,MainActivity.user.emailAddress);
                    privEvent.eventUsers.userActive.put(MainActivity.user.emailAddress,true);
                    privEvent.eventUsers.userPerm.put(MainActivity.user.emailAddress,
                            new Permission(2));
                    MainActivity.user.myevents.add(privEvent);


                    FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    MyEventsFragment myEventsFragment = new MyEventsFragment();
                    ft.replace(R.id.content_frame, myEventsFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }

                break;

        }
    }

    private boolean validateEventName(String eventname) {
        if(!eventname.matches("[A-Za-z0-9 .'_-]*")) {
            title_text.setText(R.string.invalid_eventname);
            return false;
        }
        else if (eventname.length() == 0 || eventname.length() > 24) {
            title_text.setText(R.string.invalid_eventname2);
            return false;
        }
        return true;
    }
    private boolean validateEventDesc(String eventdesc) {

        if (eventdesc.length() > 300) {
            desc_text.setText(R.string.invalid_eventdesc);
            return false;
        }
        return true;
    }

    private boolean validateDateText(String datetext, boolean isStart) {

        if (datetext.length() == 10 || datetext.length() == 6 || datetext.length() == 0) {
            if (isStart) {
                start_text.setText(R.string.invalid_datetext);
            } else {
                end_text.setText(R.string.invalid_datetext);
            }
            return false;
        }
        return true;
    }
    private boolean validateLocation(Location latLon) {
        //TODO what if location is off?
        if (latLon.latitude == MainActivity.user.myLocation.latitude &&
                latLon.longitude == MainActivity.user.myLocation.longitude) {
            return false;
        }
        return true;
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.event_type:
                if (isChecked) {

                    eventTypeText.setText("Private Event");
                    eventType = false;
                } else {

                    eventTypeText.setText("Public Event");
                    eventType = true;
                }

        }

    }
}
