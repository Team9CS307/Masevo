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
public class UserListAdapter extends BaseAdapter {
    private final ArrayList mData;

    public UserListAdapter(Map<String, Permission> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Permission> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.curr_event_row, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Permission> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.username)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.perm_level)).setText(item.getValue().getPermissionLevel() + "");

        return result;
    }
}