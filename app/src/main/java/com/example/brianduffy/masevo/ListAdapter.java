package com.example.brianduffy.masevo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brian Duffy on 10/22/2017.
 */

class ListAdapter extends ArrayAdapter<PublicEvent> {

    Context context;
    ArrayList<PublicEvent> data;
    private static LayoutInflater inflater = null;

    public ListAdapter(Context context, ArrayList<PublicEvent> data) {
        super(context, R.layout.fragment_my_events,data );

//        Event[] s = (Event[])data.toArray();
//        String g = s[0].eventName;
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public PublicEvent getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //View vi = convertView;
        Event e = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.header);
        TextView tvHome = (TextView) convertView.findViewById(R.id.text);
        // Populate the data into the template view using the data object
        tvName.setText(e.eventName);
        tvHome.setText(e.eventDesc);
        // Return the completed view to render on screen
        return convertView;
    }
}