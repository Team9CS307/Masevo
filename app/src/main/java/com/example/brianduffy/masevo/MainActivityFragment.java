package com.example.brianduffy.masevo;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import static android.R.id.list;
import static com.example.brianduffy.masevo.R.id.icon;
import static com.example.brianduffy.masevo.R.id.swiperef;
import static com.example.brianduffy.masevo.R.id.textView;
//import static com.example.brianduffy.masevo.R.id.thing_proto;

/**
 * Created by Brian on 9/18/2017.
 */

public class MainActivityFragment extends Fragment implements View.OnClickListener {
    static TextView textView;
    EditText one;
    ArrayList<String> list = new ArrayList<>();
    Server server;
    User user;
    ArrayList<String> input = new ArrayList<>();
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_activity,container,false);



        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */

        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       textView = (TextView) getView().findViewById(R.id.textView);
        one = (EditText)getView().findViewById(R.id.editText);

        getView().findViewById(R.id.create_public_event).setOnClickListener(this);
        getView().findViewById(R.id.create_private_event).setOnClickListener(this);
        getView().findViewById(R.id.enter).setOnClickListener(this);
        textView.setText(LoginActivity.emailAddress);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.create_public_event:
                textView.setText("");
                Server s = new Server();
                s.getPublicEvents();
                s = null;
                //sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");

                break;
            case R.id.create_private_event:
                textView.setText("");
                int ID = Integer.parseInt(one.getText().toString());
                s = new Server();
                s.deleteEvent(ID);
                s = null;
                //sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");

                list.clear();
                break;
            case R.id.enter:
                boolean verbose = verifyInput();
                switch (count++) {
                    case 0:
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Enter the description of the event.");
                        }
                        one.setText("");
                        break;
                    case 1:
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Enter the start time\nformat: yyyy-mm-dd hh:mm");
                        }
                        one.setText("");
                        one.setInputType(InputType.TYPE_CLASS_DATETIME);
                        break;
                    case 2:
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Enter the end time\nformat: yyyy-mm-dd hh:mm");
                        }
                        one.setText("");
                        break;
                    case 3:
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Enter longitude and latitude separated by space");
                        }
                        String loglat = (float)MainActivity.location.getLatitude() +
                                " " + (float)MainActivity.location.getLongitude();
                        one.setText(loglat);
                        one.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case 4:
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Enter a radius");
                        }
                        one.setText("");
                        break;
                    case 5:
                        //Verify
                        input.add(one.getText().toString());
                        if (verbose) {
                            textView.setText("Event Created");
                        }
                        one.setText("");
                        count = 0;

                        if (!eventCreationVerify()) {
                            return;
                        }

                        MyEventsFragment myEventsFragment = new MyEventsFragment();
                        this.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, myEventsFragment)
                                .addToBackStack(null)
                                .commit();
                        //TODO create event Date startTime, Date endTime, float latitude, float longitude,
                    //String eventName, String eventDesc, int radius, String creatorEmail
                        break;
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean eventCreationVerify () {
        //Create variables
        String eventName;
        String eventDesc;
        java.sql.Date jud1 = null;
        java.sql.Date jud2 = null;
        float longitude;
        float latitude;
        float radius;
        Date startDate;
        Date endDate;
        SimpleDateFormat sdf;
        String email = "jchambers5674@gmail.com";
        //String email = LoginActivity.emailAddress;

        try {
            eventName = input.get(0);
            eventDesc = input.get(1);
        } catch (IndexOutOfBoundsException ioobe) {
            count = 0;
            ioobe.printStackTrace();
            return false;
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //Error Checking
        try {
            jud1 = new Date(sdf.parse(input.get(2)).getTime());
            startDate = new Date(jud1.getTime());
        } catch (ParseException pe) {
            //Invalid startDate
            count = 0;
            pe.printStackTrace();
            return false;
        }
        try {
            jud2 = new Date(sdf.parse(input.get(3)).getTime());
            endDate = new Date(jud2.getTime());
            longitude = Float.parseFloat(input.get(4).split(" ")[0]);
            latitude = Float.parseFloat(input.get(4).split(" ")[1]);
            radius = Float.parseFloat(input.get(5));
        } catch (ParseException e) {
            //Invalid Date
            count = 0;
            e.printStackTrace();
            return false;
        } catch (NumberFormatException nfe) {
            //Invalid Lat and Long
            count = 0;
            nfe.printStackTrace();
            return false;
        } catch (IndexOutOfBoundsException ioobe) {
            count = 0;
            ioobe.printStackTrace();
            return false;
        }

        PublicEvent e = new PublicEvent(eventName, eventDesc, startDate, endDate,
                latitude, longitude, radius,email);
        e.createPublicEvent();
        MainActivity.user.events.add(e);
        textView.setText(Arrays.toString(input.toArray()));
        input.clear();

        return true;
    }

    public boolean verifyInput () {
        String input = one.getText().toString();
        SimpleDateFormat sdf;
        switch (count) {
            case 0:
                //Event Name
                if (!input.matches("[A-Za-z0-9 .'_-]*")) {
                    textView.setText("Event names can only contain \".'_-\" and alphanumeric characters");
                    count--;
                    return false;
                }
                if (input.length() == 0 || input.length() > 24) {
                    textView.setText("Event names must not be empty and must be 24 characters or less!");
                    count--;
                    return false;
                }
                return true;
            case 1:
                //Event Desc
                if (input.length() > 300) {
                    textView.setText("Event descriptions must be at or below 300 characters");
                    //TODO: Add character counter on key press
                    count--;
                    return false;
                }
                return true;
            case 2:
                //Start Date
                textView.setText(input);
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    sdf.parse(input);
                } catch (ParseException pe) {
                    textView.setText("Data must be in the form \"yyyy-MM-dd hh:mm\"");
                    count--;
                    return false;
                }
                return true;
            case 3:
                //End Date
                textView.setText(input);
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    sdf.parse(input);
                } catch (ParseException pe) {
                    textView.setText("Data must be in the form \"yyyy-MM-dd hh:mm\"");
                    count--;
                    return false;
                }
                return true;
            case 4:
                break;
            default:
                return false;
        }

        return true;
    }
}