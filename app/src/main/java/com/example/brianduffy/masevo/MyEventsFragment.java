package com.example.brianduffy.masevo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Brian on 9/18/2017.
 */

public class MyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
  //  User user = new User("brian",0f,0f,new HashSet<Integer>(),new HashSet<Integer>());
    static ArrayList<PublicEvent> mockeventlist = new ArrayList<>();
    ListView listview;
    SwipeRefreshLayout swipe;
    public static Event event;
    ArrayList<PublicEvent> testList = new ArrayList<>();
    static int listindex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO get user eventlist and insert into arraylist of events, then populate it with list adapter

        testList = MainActivity.user.events; //TODO TODO use this to populate stuff
        MainActivity.user.myevents = new ArrayList<>();
        MainActivity.user.myevents.add(new PublicEvent("name","desc",
                new Date(100000),new Date(100000), 0.0f,0.0f, 200f,"email"));

        //android.widget.ListAdapter listAdapter = new ListAdapter(this.getContext(),(String[])mockeventlist.toArray());
        View v = inflater.inflate(R.layout.fragment_my_events,container,false);
         listview = v.findViewById(R.id.listview);

        //System.out.println(Arrays.toString(mockeventlist.toArray()));

        listview.setAdapter(new ListAdapter(this.getContext(), MainActivity.user.myevents));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                event = MainActivity.user.myevents.get(itemPosition);

                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EventMapFragment mapFragment = new EventMapFragment();
//                EventMapFragment eventMapFragment = new EventMapFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, nearbyEventsFragment)
//                        .addToBackStack(null)
//                        .commit();
                Bundle bundle = new Bundle();

                // this event; GET data about event that was chosen
                bundle.putSerializable("event", event);
                mapFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, mapFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        registerForContextMenu(listview);

        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        swipe = v.findViewById(R.id.swiperef);
        swipe.setOnRefreshListener(this);
        updateList();
        return v;
    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //TODO
//    }
@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    if (v.getId()==R.id.listview) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {

            case R.id.edit:

                event = MainActivity.user.myevents.get((info).position);

                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EditEventFragment editFragment = new EditEventFragment();
                Bundle bundle = new Bundle();

                // this event; GET data about event that was chosen
                bundle.putSerializable("eventedit", event);
                editFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, editFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.leave:
                MainActivity.user.nearby.add(MainActivity.user.events.
                        get((info).position));

                MainActivity.user.events.remove((info).position);
                updateList();
                return true;
            case R.id.delete:
                MainActivity.user.events.remove((info).position);
                updateList();
                //TODO remove event from database **********************


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {

        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        EventMapFragment mapFragment = new EventMapFragment();

        Bundle bundle = new Bundle();
        Event event =  null;
        // this event; GET data about event that was chosen
        bundle.putSerializable("event", event);
        mapFragment.setArguments(bundle);
        ft.replace(android.R.id.content, mapFragment);
        ft.addToBackStack(null);
        ft.commit();
//

    }
    public void updateList() {

        //TODO update via server call or something
        listview.setAdapter(new ListAdapter(this.getContext(), MainActivity.user.myevents));
        swipe.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateList();
    }
}
