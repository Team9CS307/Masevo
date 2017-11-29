package com.example.brianduffy.masevo;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
//import static com.example.brianduffy.masevo.R.id.thing_proto;

/**
 * Created by Brian on 9/18/2017.
 */

public class MainActivityFragment extends Fragment implements View.OnClickListener {
   TextView priv_text;
   EditText priv_edit;
   Button priv_button;
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

        priv_button.setOnClickListener(this);

    }



    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.join_p:
                //TODO verify valid id

                if (isValidID(priv_edit.getText().toString())) {
                    //TODO server call


                    //TODO go to myevents fragem
                    FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    MyEventsFragment myEventsFragment = new MyEventsFragment();
                    ft.replace(R.id.content_frame, myEventsFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                } else {
                    priv_text.setText("Error: You entered an invalid ID!");

                }
                break;
        }
    }

   public boolean isValidID(String id) {

        return false;
   }


}