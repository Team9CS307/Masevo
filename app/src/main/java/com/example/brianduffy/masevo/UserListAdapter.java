package com.example.brianduffy.masevo;

/**
 * Created by Brian Duffy on 11/22/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
    public class UserListAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> data;
        private static LayoutInflater inflater = null;

        public UserListAdapter(Context context, ArrayList<String> data) {
            super(context, R.layout.fragment_event_map,data );

//        Event[] s = (Event[])data.toArray();
//        String g = s[0].eventName;
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View vi = convertView;
            String e = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.curr_event_row, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.username);

            tvName.setText(e);

            // Return the completed view to render on screen
            return convertView;
        }
}