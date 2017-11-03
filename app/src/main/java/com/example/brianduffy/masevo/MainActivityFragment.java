package com.example.brianduffy.masevo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import static com.example.brianduffy.masevo.R.id.textView;

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
    }


    @Override
    public void onClick(View view) {

        SimpleDateFormat sdf;
        switch (view.getId()) {

            case R.id.create_public_event:
                textView.setText("");
                 sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");

                break;
            case R.id.create_private_event:
                textView.setText("");
                sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");

                list.clear();
                break;
            case R.id.enter:
                switch (count) {
                    case 0:
                        count++;
                        input.add(one.getText().toString());
                        textView.setText("Enter the description of the event.");
                        one.setText("");

                        break;
                    case 1:
                        count++;
                        input.add(one.getText().toString());
                        textView.setText("Enter the start time\nformat: yyyy-mm-dd hh:mm");
                        one.setText("");
                        one.setInputType(InputType.TYPE_CLASS_DATETIME);



                        break;
                    case 2:
                        count++;
                        input.add(one.getText().toString());
                        textView.setText("Enter the end time\nformat: yyyy-mm-dd hh:mm");
                        one.setText("");


                        break;
                    case 3:
                        count++;
                        input.add(one.getText().toString());
                        textView.setText("Enter longitude and latitude separated by space");
                        one.setText("");
                        one.setInputType(InputType.TYPE_CLASS_TEXT);



                        break;
                    case 4:
                        count++;
                        input.add(one.getText().toString());
                        textView.setText("Enter your email address");
                        one.setText("");
                        one.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;
                    case 5:
                        input.add(one.getText().toString());
                        textView.setText("Event Created");
                        one.setText("");
                        count = 0;
                        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        java.util.Date jud1 = null;
                        java.util.Date jud2 = null;

                        try {
                            jud1 = sdf.parse(input.get(2));
                            jud2 = sdf.parse(input.get(3));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String [] lonlat = input.get(4).split(" ");
                        Date startDate = new Date(jud1.getTime());
                        Date endDate = new Date(jud2.getTime());
                        Event e = new PublicEvent(startDate,endDate,Float.parseFloat(lonlat[0]),
                                Float.parseFloat(lonlat[1]),input.get(0),input.get(1),500,input.get(5));
                         textView.setText(Arrays.toString(input.toArray()));
                                input.clear();

                       MainActivity.user.events.add(e);
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





}
