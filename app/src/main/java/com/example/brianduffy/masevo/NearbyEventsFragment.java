package com.example.brianduffy.masevo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.brianduffy.masevo.MyEventsFragment.mockeventlist;

/**
 * Created by Brian on 9/18/2017.
 */

public class NearbyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView listview;
    private Event event;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nearby_events,container,false);

        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        listview = (ListView) v.findViewById(R.id.listview);

        //System.out.println(Arrays.toString(mockeventlist.toArray()));

        listview.setAdapter(new ListAdapter(this.getContext(), MainActivity.user.nearby));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                event = MainActivity.user.nearby.get(itemPosition);
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EventMapFragment mapFragment = new EventMapFragment();
//
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listview) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.nearby_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {


            case R.id.join:
                MainActivity.user.myevents.add(MainActivity.user.nearby.
                        get(((AdapterView.AdapterContextMenuInfo)info).position));

                MainActivity.user.nearby.remove(((AdapterView.AdapterContextMenuInfo)info).position);
                updateList();

                // TODO Server call

                return true;



            default:
                return super.onContextItemSelected(item);
        }
    }

    public void updateList() {
        listview.setAdapter(new ListAdapter(this.getContext(), MainActivity.user.nearby));
        swipe.setRefreshing(false);


    }
    @Override
    public void onRefresh() {
        updateList();
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
    }
}
