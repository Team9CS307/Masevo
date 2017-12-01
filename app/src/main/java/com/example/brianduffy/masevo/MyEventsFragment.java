package com.example.brianduffy.masevo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
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
import android.widget.Toast;

import com.example.chambe41.masevo.ThreadDeleteEvent;
import com.example.chambe41.masevo.ThreadLeaveEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Brian on 9/18/2017.
 */

public class MyEventsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    ListView listview;
    SwipeRefreshLayout swipe;
    public static Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_events,container,false);
         listview = v.findViewById(R.id.listview);


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
                // TODO handle the changing from public to private event


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
                boolean del = true;
                Event temp = MainActivity.user.myevents.get((info).position);
                Boolean isPub;
                if (temp instanceof PublicEvent) {
                    isPub = true;
                } else {
                    isPub = false;
                }
                ThreadLeaveEvent threadLeaveEvent = new ThreadLeaveEvent(temp.eventID,MainActivity.user.emailAddress,isPub);

                Thread thread = new Thread(threadLeaveEvent);
                Pair<Boolean,Integer> ret;
                thread.start();
                try {
                    thread.join();
                    ret = threadLeaveEvent.getReturnResult();

                    if (ret.second!=0) {
                        Toast.makeText(getContext(),"errno",Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                // check to see if user has creator permissions
                if (temp.eventUsers.userPerm.get(MainActivity.user.emailAddress).hasCreatorPermissions()) {
                    for (Map.Entry<String,Permission> entry: temp.eventUsers.userPerm.entrySet()) {
                        // find user who has host privlages and set them to creator
                        if (entry.getValue().hasHostPermissions()) {
                            entry.setValue(new Permission(2));
                            del = false;
                            break;
                        }
                    }
                    //remove leaver from eventUsers
                    temp.eventUsers.userPerm.remove(MainActivity.user.emailAddress);
                    temp.eventUsers.userActive.remove(MainActivity.user.emailAddress);

                }
                if (temp instanceof PublicEvent && !del) {

                    //TODO figure out for nearby events
                    MainActivity.user.nearby.add(MainActivity.user.myevents.
                            get((info).position));
                }

                MainActivity.user.myevents.remove((info).position);

                updateList();
                return true;
            case R.id.delete:
                    // only delete if user has creator permissions
                Event event = MainActivity.user.myevents.get((info).position);
                    Boolean isPub1;
                    if (event instanceof PublicEvent) {
                        isPub = true;
                    } else {
                        isPub = false;
                    }
                ThreadDeleteEvent threadDeleteEvent = new ThreadDeleteEvent(event.eventID,MainActivity.user.emailAddress,isPub);

                Thread thread1 = new Thread(threadDeleteEvent);
                Pair<Boolean,Integer> ret1;
                thread1.start();
                try {
                    thread1.join();
                    ret1 = threadDeleteEvent.getReturnResult();

                    if (ret1.second != 0) {
                        Toast.makeText(getContext(),"errno",Toast.LENGTH_LONG).show();
                        return false;
                    }else  {
                        //client side update
                        MainActivity.user.myevents.remove((info).position);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (MainActivity.user.myevents.get((info).position).eventUsers.userPerm.
                        get(MainActivity.user.emailAddress).hasCreatorPermissions()) {
                    MainActivity.user.myevents.remove((info).position);

                }
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
