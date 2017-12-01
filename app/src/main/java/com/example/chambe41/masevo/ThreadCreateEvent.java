package com.example.chambe41.masevo;

import android.content.ContentValues;
import android.util.Pair;

import com.example.brianduffy.masevo.DatePickerFragment;
import com.example.brianduffy.masevo.Event;
import com.example.brianduffy.masevo.Location;
import com.example.brianduffy.masevo.PublicEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Brian Duffy on 11/30/2017.
 */

public class ThreadCreateEvent implements Runnable {
    private final String properties = "ID,Name,Description,StartTime,EndTime," +
            "Latitude,Longitude,Radius,Host,List";

    private final String server_url = "http://webapp-171031005244.azurewebsites.net";

    Event event;
    boolean pub;
    Date startDate;
    Date endDate;
    String hostEmail;
    int eventID;
    String eventName;
    String eventDesc;
    Location location;
    float radius;
    private Pair<Event, Integer> returnResult;

    public ThreadCreateEvent(String eventName,String eventDesc,Date startDate,Date endDate,
                             Location location, float radius,int eventID, String hostEmail,boolean pub) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.eventID = eventID;
        this.hostEmail = hostEmail;
        this.location = location;
        this.radius = radius;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pub = pub;

    }
    public boolean createEvent() {
        String methodName;
        if (pub) {
             methodName = "createPublicEvent";
        } else {
            methodName = "createPrivateEvent";
        }
        String emailTrim = hostEmail;

        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
        contentValues.put("ID",Integer.toString(eventID));
        contentValues.put("Name",eventName);
        contentValues.put("Description",eventDesc);
        contentValues.put("StartTime",Long.toString(startDate.getTime()));
        contentValues.put("EndTime",Long.toString(endDate.getTime()));
        contentValues.put("Latitude",Float.toString(location.latitude));
        contentValues.put("Longitude",Float.toString(location.longitude));
        contentValues.put("Radius",Float.toString(radius));
        contentValues.put("Host",emailTrim);

        //TODO create the actual event

        String query = "";
        for (Map.Entry e: contentValues.valueSet()) {
            query += (e.getKey() + "=" + e.getValue() + "&");
        }
        query = query.substring(0, query.length() - 1);
        final String fQuery = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] postData = fQuery.getBytes(StandardCharsets.UTF_8);
                    int postDataLength = postData.length;
                    URL url = new URL(server_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    httpURLConnection.setRequestProperty("charset", "utf-8");
                    httpURLConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    httpURLConnection.setUseCaches(false);
                    try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
                        dataOutputStream.write(postData);
                        dataOutputStream.flush();
                    }
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        String result = "";
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(httpURLConnection.getInputStream()));
                        String output;
                        while((output = br.readLine()) != null)
                        {
                            result += output;
                        }
                        System.out.println("Response message: " + result);
                    }
                } catch (MalformedURLException murle) {
                    murle.printStackTrace();
                    return;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    return;
                }
            }
        }).start();


        return false;
    }
    public Pair<Event, Integer> getReturnResult() {

        return returnResult;
    }
    @Override
    public void run() {
        //TODO maybe?
        createEvent();
    }
}
