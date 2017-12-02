package com.example.brianduffy.masevo;

/**
 * Created by Brian Duffy on 11/11/2017.
 */
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.chambe41.masevo.ThreadModifyEvent;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian Duffy on 11/9/2017.
 */

public class EditEventFragment extends android.support.v4.app.Fragment implements View.OnClickListener,
Switch.OnCheckedChangeListener{
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
    Button getLoc;
    Boolean eventType = true; // true is public, false is private
    TextView eventTypeText;
    Event event;
    Button editButton;
    Permission userPerm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view

        // get data from a previous activity unpack bundle
        Bundle bundle = getArguments();

        // build copy of serilaized data with key eventedit
        event= (Event) bundle.getSerializable("eventedit");

        // expand the view
        return inflater.inflate(R.layout.create_event_layout,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // initialize the instance variables
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
        editButton = getView().findViewById(R.id.create_event);

        // Set onclick listeners to buttons
        eventSwitch.setOnCheckedChangeListener(this);
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        getLoc.setOnClickListener(this);
        editButton.setOnClickListener(this);
        editButton.setText("Edit Event");

        //repopulate fields with event data
        title.setText(event.eventName);
        desc.setText(event.eventDesc);
        latitude = event.location.latitude;
        longitude = event.location.longitude;
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        //if (event.startDate)
        start_text.setText(sfd.format(event.startDate));
        end_text.setText(sfd.format(event.endDate));
        if (event instanceof PublicEvent) {
            eventType = true;
            eventSwitch.setChecked(false);
        } else {
            eventType = false;
            eventSwitch.setChecked(true);
        }

        // Get the user perm of person changing the event
        for(Map.Entry<String,Permission> entry: event.eventUsers.userPerm.entrySet()) {
            if (MainActivity.user.emailAddress.equals(entry.getKey())) {
                userPerm = entry.getValue();
                break;
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle place picker request
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data);

                // store lat and lon for later use
                latitude = (float) place.getLatLng().latitude;
                longitude =(float) place.getLatLng().longitude;

                // build toast message to let user know location
                String toastMsg = "latitude: " + latitude + " longitude: " + longitude;
                Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_date: // start date button id

                //clear the textview
                start_text.setText("");

                // build timepicker
                DialogFragment timestart = new TimePickerFragment();
                timestart.show(getActivity().getFragmentManager(),"timeSPicker");

                // build datepicker
                DialogFragment start = new DatePickerFragment();
                start.show(getActivity().getFragmentManager(), "startPicker");

                break;
            case R.id.end_date: // end date button id

                //clear the textview
                end_text.setText("");

                // build timepicker
                DialogFragment timeend = new TimePickerFragment();
                timeend.show(getActivity().getFragmentManager(),"timeEPicker");

                // build datepicker
                DialogFragment end = new DatePickerFragment();
                end.show(getActivity().getFragmentManager(), "endPicker");

                break;
            case R.id.create_event: // create event button id

                for (int i = 0 ; i < MainActivity.user.myevents.size(); i++) {
                    if (MainActivity.user.myevents.get(i) == event) {
                        MainActivity.user.myevents.remove(i);
                    }
                }

                String eventName = title.getText().toString();
                String eventDesc = desc.getText().toString();
                float radius;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

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
                    // checkParamChanged modifies event if true
                    if (!checkParamChanged(eventName,eventDesc,jud1,jud2,latitude,longitude,true)) {
                        Toast.makeText(getContext(),"Nothing has been changed...",Toast.LENGTH_LONG).show();
                    } else {

                        Event temp = new PublicEvent(eventName,eventDesc,jud1,jud2,
                                latitude,longitude,event.radius,event.hostEmail);

                        //Server call
                        ThreadModifyEvent modifyEvent = new ThreadModifyEvent(event.eventID,temp.eventName,temp.eventDesc,
                                temp.startDate,temp.endDate,temp.location.latitude,temp.location.longitude,temp.radius,event.hostEmail,eventType);
                        Thread thread = new Thread(modifyEvent);
                        thread.start();
                        try {
                            thread.join();
                            Pair<Event,Integer> ret = modifyEvent.getReturnResult();
                            if (ret.second != 0) {
                                Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                                        .getErrorMessage(ret.second),Toast.LENGTH_LONG).show();
                                return;
                            }else {
                                //todo success
                                int id = event.eventID;
                                event = temp;
                                event.eventID = id;

                                int index = 0;
                                // find and remove old event put new in
                                for (Event e : MainActivity.user.myevents) {
                                    if (e.eventID == event.eventID) {
                                        MainActivity.user.myevents.remove(index);
                                        MainActivity.user.myevents.add(index,event);
                                        break;
                                    }
                                    index++;
                                }



                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                      //  temp.eventUsers = event.eventUsers;

                        NotificationManager notifManager=(NotificationManager)getActivity()
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        String eventData = "Event: " + event.eventName + "\n" +
                                "Description: " + event.eventDesc + "\n" +
                                "Start Date: " + event.startDate.toString() + "\n" +
                                "End Date: " + event.endDate.toString() + "\n" +
                                "Location: Latitude = " + event.location.latitude +
                                " Longitude = " + event.location.longitude;

                        Notification notification = new Notification.Builder(getContext().
                                getApplicationContext()).setSmallIcon(R.mipmap.ic_masevo_app)
                                .setContentTitle("Masevo Event Changed").setContentText(eventData).build();

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        notifManager.notify(NotificationManager.IMPORTANCE_HIGH, notification);


                    }

                } else {
                    // checkParamChanged modifies event if true
                    if (!checkParamChanged(eventName,eventDesc,jud1,jud2,latitude,longitude,false)) {
                        Toast.makeText(getContext(),"Nothing has been changed...",Toast.LENGTH_LONG).show();
                    } else {
                        Event temp = new PrivateEvent(eventName,eventDesc,jud1,jud2,
                                latitude,longitude,event.radius,event.hostEmail);

                        ThreadModifyEvent modifyEvent = new ThreadModifyEvent(event.eventID,temp.eventName,temp.eventDesc,
                                temp.startDate,temp.endDate,temp.location.latitude,temp.location.longitude,temp.radius,event.hostEmail,eventType);
                        Thread thread = new Thread(modifyEvent);
                        thread.start();
                        try {
                            thread.join();
                            Pair<Event,Integer> ret = modifyEvent.getReturnResult();
                            if (ret.second != 0) {
                                Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                                        .getErrorMessage(ret.second),Toast.LENGTH_LONG).show();
                                return;
                            }else {
                                //todo success
                                int id = event.eventID;
                                event = temp;
                                event.eventID = id;
                                int index = 0;
                                // find and remove old event put new in
                                for (Event e : MainActivity.user.myevents) {
                                    if (e.eventID == event.eventID) {
                                        MainActivity.user.myevents.remove(index);
                                        MainActivity.user.myevents.add(index,event);
                                        break;
                                    }
                                    index++;
                                }



                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        NotificationManager notifManager=(NotificationManager)getActivity()
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        String eventData = "Event: " + event.eventName + "\n" +
                                "Description: " + event.eventDesc + "\n" +
                                "Start Date: " + event.startDate.toString() + "\n" +
                                "End Date: " + event.endDate.toString() + "\n" +
                                "Location: Latitude = " + event.location.latitude +
                                " Longitude = " + event.location.longitude;

                        Notification notification = new Notification.Builder(getContext().
                                getApplicationContext()).setSmallIcon(R.mipmap.ic_masevo_app)
                                .setContentTitle("Masevo Event Changed").setContentText(eventData).build();

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        notifManager.notify(NotificationManager.IMPORTANCE_HIGH, notification);

                    }



                }


                //TODO will need to repopulate the event after the event has been modified.

                // return to where the user was when they first initiated edit event
                getActivity().onBackPressed();
                break;
            case R.id.location_but: // location button id

                // create placepicker
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

    public boolean checkParamChanged(String eventname, String desc, Date start, Date end,
                                  float lat, float lon, boolean type) {
        //type = true : public event
        if (!eventname.equals(event.eventName) || !desc.equals(event.eventDesc) ||
                !start.equals(event.startDate) || !end.equals(event.endDate) ||
                lat != event.location.latitude ||  lon != event.location.longitude ||
                type && event instanceof PrivateEvent || !type && event instanceof PublicEvent) {
                event.modifyEvent(event.eventID,eventname,desc,start,end,lat,lon,event.radius, event.hostEmail);

            return true;
        }

        return false;
    }
    // used to check for valid eventname
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
    // used to check for valid eventDesc
    private boolean validateEventDesc(String eventdesc) {

        if (eventdesc.length() > 300) {
            desc_text.setText(R.string.invalid_eventdesc);
            return false;
        }
        return true;
    }

    // used to check for valid date and time
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

    //used to check for valid location TODO what if user doesnt want to edit the event's location?
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
