package com.example.brianduffy.masevo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.example.brianduffy.masevo.MainActivity.mGeofenceRequestIntent;
import static com.example.brianduffy.masevo.MainActivity.mGoogleApiClient;


/**
 * Created by Brian on 9/18/2017.
 */

public class NearbyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView listview;
    private Event event;
    ArrayList<Event> events = new ArrayList<>();
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nearby_events,container,false);

        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        listview = (ListView) v.findViewById(R.id.listview);

        ThreadGetEvents getEvents = new ThreadGetEvents();
        Thread t = new Thread(getEvents);
        t.start();
        try {
            t.join();
            Pair<ArrayList<? extends Event>,Integer> ret = getEvents.getReturnResult();

            if (ret.second != 0) {
                Toast.makeText(getContext(),Error.getErrorMessage(ret.second),Toast.LENGTH_SHORT).show();
            } else {
                for (Event e : ret.first) {
                    if (e instanceof PublicEvent) {
                        events.add(e);
                    }

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(Arrays.toString(mockeventlist.toArray()));

        listview.setAdapter(new ListAdapter(this.getContext(), MainActivity.user.nearby));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                event = events.get(itemPosition);
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                NearbyMapFragment mapFragment = new NearbyMapFragment();
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
            updateList();
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
                 // add user to event as a regular attendee and set thier status as active

                Event toJoin = events.get((info).position);
                Boolean isPub;
                if (toJoin instanceof PublicEvent) {
                    isPub = true;
                }else {
                    isPub = false;
                }
                ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(toJoin.eventID,MainActivity.user.emailAddress,isPub);
                Thread thread = new Thread(threadJoinEvent);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Pair<Boolean,Integer> ret = threadJoinEvent.getReturnResult();

                if (ret.second != 0) {
                    Toast.makeText(getContext(), com.example.brianduffy.masevo.Error
                            .getErrorMessage(ret.second),Toast.LENGTH_LONG).show();
                } else {
                    //TODO implement success

                    MainActivity.user.myevents.add(toJoin);
                    MainActivity.mGeofenceList.add(new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(toJoin.eventName)

                            // Set the circular region of this geofence.
                            .setCircularRegion(
                                    toJoin.location.latitude,
                                    toJoin.location.longitude,
                                    toJoin.radius
                            )
                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            // Create the geofence.
                            .build());
                    //TODO comment out for espresso testing
                    if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return false;
                    }
                    LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                            MainActivity.getGeofencingRequest(), mGeofenceRequestIntent);

                    events.remove((info).position);
                }
                updateList();

                // TODO Server call

                return true;



            default:
                return super.onContextItemSelected(item);
        }
    }

    public void updateList() {
        listview.setAdapter(new ListAdapter(this.getContext(),events));
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
        NearbyMapFragment mapFragment = new NearbyMapFragment();

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
