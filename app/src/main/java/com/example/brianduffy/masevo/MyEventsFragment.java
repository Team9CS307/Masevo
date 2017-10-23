package com.example.brianduffy.masevo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Brian on 9/18/2017.
 */

public class MyEventsFragment extends Fragment implements View.OnClickListener {
  //  User user = new User("brian",0f,0f,new HashSet<Integer>(),new HashSet<Integer>());
    ArrayList<Event> mockeventlist = new ArrayList<>();
    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO get user eventlist and insert into arraylist of events, then populate it with list adapter
        float lat = 0f;
        float lon = 1f;
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 1","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 2","event description 2",500,"String email"));

        //android.widget.ListAdapter listAdapter = new ListAdapter(this.getContext(),(String[])mockeventlist.toArray());
        View v = inflater.inflate(R.layout.fragment_my_events,container,false);
        ListView lv = (ListView) v.findViewById(R.id.listview);

        //System.out.println(Arrays.toString(mockeventlist.toArray()));
        lv.setAdapter(new ListAdapter(this.getContext(), mockeventlist));
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */

        return v;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //TODO
//    }

    @Override
    public void onClick(View v) {
        //TODO find way to generalize potentially infinite buttons that can be pressed
    }
}
