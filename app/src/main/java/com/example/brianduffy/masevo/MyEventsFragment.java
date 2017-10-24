package com.example.brianduffy.masevo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Brian on 9/18/2017.
 */

public class MyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
  //  User user = new User("brian",0f,0f,new HashSet<Integer>(),new HashSet<Integer>());
    ArrayList<Event> mockeventlist = new ArrayList<>();
    ListView listview;
    SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO get user eventlist and insert into arraylist of events, then populate it with list adapter
        float lat = 0f;
        float lon = 1f;
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 1","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 2","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 3","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 4","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 5","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 6","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 7","event description",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));
        mockeventlist.add(new PublicEvent(new Date(3),new Date(500),lat,lon,
                "Public Event 8","event description 2f oifj wqoifj ep;ofi2jew pofijefo ijfepoqfijw",500,"String email"));

        //android.widget.ListAdapter listAdapter = new ListAdapter(this.getContext(),(String[])mockeventlist.toArray());
        View v = inflater.inflate(R.layout.fragment_my_events,container,false);
         listview = (ListView) v.findViewById(R.id.listview);

        //System.out.println(Arrays.toString(mockeventlist.toArray()));
        listview.setAdapter(new ListAdapter(this.getContext(), mockeventlist));
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        swipe = v.findViewById(R.id.swiperef);
        swipe.setOnRefreshListener(this);
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
    public void updateList() {
        mockeventlist.remove(1);
        listview.setAdapter(new ListAdapter(this.getContext(), mockeventlist));
        swipe.setRefreshing(false);


    }
    @Override
    public void onRefresh() {
        updateList();
    }
}
