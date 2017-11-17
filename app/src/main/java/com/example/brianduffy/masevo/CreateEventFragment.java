package com.example.brianduffy.masevo;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian Duffy on 11/9/2017.
 */

public class CreateEventFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    TextView title_text;
    EditText title;
    TextView desc_text;
    EditText desc;
    Button start_time;
    TextView start_text;
    TextView end_text;
    int PLACE_PICKER_REQUEST = 1;
    Double latitude;
    Double longitude;

    Button end_time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view

        return inflater.inflate(R.layout.create_event_layout,container,false); // TODO maybe true
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

        // Set onclick listenters to buttons

        getView().findViewById(R.id.start_date).setOnClickListener(this);
        getView().findViewById(R.id.end_date).setOnClickListener(this);
        getView().findViewById(R.id.location_but).setOnClickListener(this);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) { // Response from google Place Picker Activity
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data); // Get place data

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                String toastMsg = "latitude: " + latitude + " longitude: " + longitude; // Display

                Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_LONG).show();
                //TODO now use data

            }
        }
    }

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
                //TODO the timepicker is started first because it is at the bottom of the view stack
                //TODO then the datepicker is put on top. Once datepicker is done, timepicker moves to view.

                DialogFragment timeend = new TimePickerFragment();
                timeend.show(getActivity().getFragmentManager(),"timeEPicker"); //start timepicker
                DialogFragment end = new DatePickerFragment();
                end.show(getActivity().getFragmentManager(), "endPicker"); // start datepicker
                break;
            case R.id.create_event: // create event button id
                //TODO add stuff here



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
        }
    }
}
