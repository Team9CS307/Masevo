package com.example.brianduffy.masevo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import static android.R.id.list;
import static com.example.brianduffy.masevo.R.id.textView;

/**
 * Created by Brian on 9/18/2017.
 */

public class MainActivityFragment extends Fragment implements View.OnClickListener{
    TextView textView;
    EditText one;
    ArrayList<String> list = new ArrayList<>();
    Server server;
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_activity,container,false);


        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */

        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = (TextView) getView().findViewById(R.id.textView);
        one = (EditText)getView().findViewById(R.id.editText);

        getView().findViewById(R.id.button2).setOnClickListener(this);
        getView().findViewById(R.id.create_public_event).setOnClickListener(this);
        getView().findViewById(R.id.create_private_event).setOnClickListener(this);
        getView().findViewById(R.id.join_private_event).setOnClickListener(this);
        getView().findViewById(R.id.join_public_event).setOnClickListener(this);
        user = new User("emailAddress",0.0f,0.0f,new HashSet<Integer>(),new HashSet<Integer>());        // or  (ImageView) view.findViewById(R.id.foo);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onClick(View view) {
        float lat = 40.4302296f;
        float lon = -86.9107470f;
        switch (view.getId()) {
            case R.id.join_public_event:
                textView.setText("");
                int eventID = Integer.parseInt(list.get(0));
                //user.joinEvent(eventID);
                user.joinEvent(new PublicEvent(new Date(3), new Date(500), lat, lon,
                        "Public Event test", "event description", 500, user.emailAddress));

                textView.setText(user.emailAddress + "joined public event ");
                list.clear();
                break;
            case R.id.join_private_event:
                textView.setText("");
                user.joinEvent(new PublicEvent(new Date(3), new Date(500), lat, lon,
                        "Private Event test", "event description", 500, user.emailAddress));

                textView.setText(user.emailAddress + "joined public event ");
                list.clear();
                break;
            case R.id.create_public_event:
                textView.setText("");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                float latitude = 40.4302296f;
                float longitude = -86.9107470f;
                String d1 = "2017-10-05 16:00";
                String d2 = "2017-10-05 17:00";
                Date startDate = null;
                Date endDate = null;
                try {
                    java.util.Date jud1 = sdf.parse(d1);
                    java.util.Date jud2 = sdf.parse(d2);
                    startDate = new Date(jud1.getTime());
                    endDate = new Date(jud2.getTime());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                String hostName = "masevo.database.windows.net";
                String dbName = "MasevoFields2";
                String user = "MASEVO_ADMIN";
                String password = "M4s3v0_4dm1n";
                server = new Server(hostName, dbName, user, password);
                PublicEvent pe = new PublicEvent(startDate, endDate, latitude,
                        longitude, list.get(0), list.get(1), Integer.parseInt(list.get(2)),
                        list.get(3), server);
                textView.setText("Event Created: " + pe.eventName + " " + pe.eventDesc +
                        " lat: " + latitude + " log:" +
                        longitude + " created by: " + list.get(3));

                list.clear();
                break;
            case R.id.create_private_event:
                textView.setText("");
                sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                latitude = 40.4302296f;
                longitude = -86.9107470f;
                d1 = "2017-10-05 16:00";
                d2 = "2017-10-05 17:00";
                startDate = null;
                endDate = null;
                try {
                    java.util.Date jud1 = sdf.parse(d1);
                    java.util.Date jud2 = sdf.parse(d2);
                    startDate = new Date(jud1.getTime());
                    endDate = new Date(jud2.getTime());
                } catch (ParseException pee) {
                    pee.printStackTrace();
                }
                hostName = "masevo.database.windows.net";
                dbName = "MasevoFields2";
                user = "MASEVO_ADMIN";
                password = "M4s3v0_4dm1n";
                server = new Server(hostName, dbName, user, password);
                PublicEvent pee = new PublicEvent(startDate, endDate, latitude,
                        longitude, list.get(0), list.get(1), Integer.parseInt(list.get(2)),
                        list.get(3), server);
                textView.setText("Event Created: " + pee.eventName + " " + pee.eventDesc +
                        " lat: " + latitude + " log:" +
                        longitude + " created by: " + list.get(3));

                list.clear();
                break;
            case R.id.button2:
                list.add(one.getText().toString());
                one.setText("");
        }
    }
}
