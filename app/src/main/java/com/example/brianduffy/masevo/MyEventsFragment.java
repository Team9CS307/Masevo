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
    static ArrayList<Event> mockeventlist = new ArrayList<>();
    ListView listview;
    SwipeRefreshLayout swipe;
    public static Event event;


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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                event = mockeventlist.get(itemPosition);
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

            case R.id.leave:
                mockeventlist.remove(((AdapterView.AdapterContextMenuInfo)info).position);
                updateList();
                return true;
            case R.id.delete:
                mockeventlist.remove(((AdapterView.AdapterContextMenuInfo)info).position);
                updateList();
                //TODO remove event from database


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        //TODO find way to generalize potentially infinite buttons that can be pressed

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
//        ArticleFragment newFragment = new ArticleFragment();
//        Bundle args = new Bundle();
//        args.putInt(ArticleFragment.ARG_POSITION, position);
//        newFragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack so the user can navigate back
//        transaction.replace(R.id.fragment_container, newFragment);
//        transaction.addToBackStack(null);
//
//        // Commit the transaction
//        transaction.commit();
        //TODO depending on what event is chosen, set static variable event to the chosen list item.
        // TODO then commit a new fragment transaction. Inside new fragment, get location and name of event
    }
    public void updateList() {
        //mockeventlist.remove(1);
        listview.setAdapter(new ListAdapter(this.getContext(), mockeventlist));
        swipe.setRefreshing(false);


    }
    @Override
    public void onRefresh() {
        updateList();
    }
}
