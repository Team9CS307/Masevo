package com.example.brianduffy.masevo;

/**
 * Created by Brian Duffy on 11/11/2017.
 */
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

public class EditEventFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
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
    Event event;
    Button end_time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view
        Bundle bundle = getArguments();
        event= (Event) bundle.getSerializable("eventedit");
        return inflater.inflate(R.layout.create_event_layout,container,false); // TODO maybe true
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title_text = getView().findViewById(R.id.event_name_text);
        title = getView().findViewById(R.id.event_name);
        desc_text = getView().findViewById(R.id.event_desc_text);
        desc = getView().findViewById(R.id.event_desc);
        start_text = getView().findViewById(R.id.start_time);
        end_text = getView().findViewById(R.id.end_time);

//        dpf = new DatePickerFragment();// ERROR
//        dpf.showDatePickerDialog(view);
        getView().findViewById(R.id.start_date).setOnClickListener(this);
        getView().findViewById(R.id.end_date).setOnClickListener(this);
        getView().findViewById(R.id.location_but).setOnClickListener(this);
//        getView().findViewById(R.id.create_private_event).setOnClickListener(this);
//        getView().findViewById(R.id.enter).setOnClickListener(this);
        title.setText(event.eventName);
        desc.setText(event.eventDesc);
        start_text.setText(event.startDate.toString());
        end_text.setText(event.endDate.toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data);

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                String toastMsg = "latitude: " + latitude + " longitude: " + longitude;
                Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_date:
                DialogFragment timestart = new TimePickerFragment();
                timestart.show(getActivity().getFragmentManager(),"timeSPicker");
                DialogFragment start = new DatePickerFragment();
                start.show(getActivity().getFragmentManager(), "startPicker");


                break;
            case R.id.end_date:

                DialogFragment timeend = new TimePickerFragment();
                timeend.show(getActivity().getFragmentManager(),"timeEPicker");
                DialogFragment end = new DatePickerFragment();
                end.show(getActivity().getFragmentManager(), "endPicker");
                break;
            case R.id.create_event:
                //TODO add stuff here
		NotificationManager notifManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		String eventData = "Event: " + event.eventName + "\n" +
				   "Description: " + event.eventDesc + "\n" +
				   "Start Date: " + event.startDate.toString() + "\n" +
				   "End Date: " + event.endDate.toString() + "\n" +
				   "Location: Latitude = " + event.location.latitude + " Longitude = " + event.location.longitude;  
	        Notification notification = new Notification.Builder(getApplicationContext()).setContentTitle("Masevo Event Changed").setContentText(eventData).build();
                
               notification.flags |= Notification.FLAG_AUTO_CANCEL;
               notifManager.notification(0, notification);

                break;
            case R.id.location_but:
                //TODO dod this
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

