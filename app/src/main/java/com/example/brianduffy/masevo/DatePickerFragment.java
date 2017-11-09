package com.example.brianduffy.masevo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Brian Duffy on 11/8/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_DARK, this, year, month, day);
    }

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getParentFragment().getFragmentManager(), "datePicker");
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
       // String s = this.getTag();

        if (this.getTag().equals("startPicker")) {
            TextView start = getActivity().findViewById(R.id.start_time);
            start.setText(String.format("%d:%02d:%02d",year,month,day));

        } else if (this.getTag().equals("endPicker")) {
            TextView end = getActivity().findViewById(R.id.end_time);
            end.setText(String.format("%d:%02d:%02d",year,month,day));
        }

    }

}
