package com.example.brianduffy.masevo;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Brian Duffy on 11/9/2017.
 */

public class CreateEventFragment extends Fragment implements View.OnClickListener{
    TextView title_text;
    EditText title;
    TextView desc_text;
    EditText desc;
    Button start_time;
    TextView start_text;
    TextView end_text;
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
//        getView().findViewById(R.id.create_private_event).setOnClickListener(this);
//        getView().findViewById(R.id.enter).setOnClickListener(this);

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
                break;
            case R.id.location_but:
                //TODO dod this
                break;
        }
    }
}
