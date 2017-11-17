package com.example.brianduffy.masevo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Brian Duffy on 11/9/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        // get the time
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        //start the timepicker activity
        return new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_DARK, this, hour, min, false);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // check to make sure the tag matches the textview you want to update
        // timeSpicker is start time picker
        // timeEpicker is time end picker
        if (this.getTag().equals("timeSPicker")) {
            // show new start time to user

            TextView start = getActivity().findViewById(R.id.start_time);
            start.append(String.format(":%02d:%02d",hourOfDay,minute));

        } else if (this.getTag().equals("timeEPicker")) {
            //show new end time to user

            TextView end = getActivity().findViewById(R.id.end_time);
            end.append(String.format(":%02d:%02d",hourOfDay,minute));
        }
    }
}
