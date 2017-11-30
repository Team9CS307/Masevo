package com.example.brianduffy.masevo;

import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event implements Serializable{
    // Use this to create a PublicEvent
    private final String properties = "ID,Name,Description,StartTime,EndTime," +
            "Latitude,Longitude,Radius,Host,List";

    private final String server_url = "http://webapp-171031005244.azurewebsites.net";

    /**
     * Used for call to server which do not require an event to be present
     */
    public PublicEvent() {};
    public PublicEvent(String eventName, String eventDesc, Date startDate, Date endDate,
                       float latitude, float longitude, float radius, String creatorEmail)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = new Location(latitude, longitude);
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.radius = radius;
        this.hostEmail = creatorEmail;

        this.hostList = new HashSet<>();
        this.attendeeList = new HashSet<>();
        this.activeList = new HashSet<>();
        this.emailToDisplay = new HashMap<>();

        this.eventID = (eventName + eventDesc + startDate + endDate + latitude + longitude +
                radius + Double.toString(Math.random())).hashCode();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createEvent() {
        String methodName = "createPublicEvent";
        String emailTrim = hostEmail.substring(0,hostEmail.indexOf("@"));

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
    }

    public ArrayList<PublicEvent> getEvents() {
        ThreadGetEvents threadGetEvents = new ThreadGetEvents();
        new Thread(threadGetEvents).start();
        return threadGetEvents.getReturnResult();
    }

    public void deleteEvent(int eventID) {
        String methodName = "deleteEvent";
        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
        contentValues.put("ID",Integer.toString(eventID));
        String query = "";
        for (Map.Entry e: contentValues.valueSet()) {
            query += (e.getKey() + "=" + e.getValue() + "&");
        }
        query = query.substring(0, query.length() - 1);
        final String fQuery = query;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }

    @Override
    public void joinEvent(int eventID) {

    }

    @Override
    public void leaveEvent(int eventID) {

    }

    @Override
    public void addUser(int eventID, String email) {

    }

    @Override
    public void removeUser(int eventID, String email) {

    }

    @Override
    public void banUser(int eventID, String email) {

    }

    @Override
    public void makeOwner(int eventID, String email) {

    }

    @Override
    public void makeHost(int eventID, String email) {

    }

    @Override
    public void makeUser(int eventID, String email) {

    }

    public void modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate,
                            float latitude, float longitude, float radius, String hostEmail) {
        String methodName = "modifyEvent";
        String emailTrim = hostEmail.substring(0,hostEmail.indexOf("@"));

        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
        contentValues.put("ID",Integer.toString(eventID));
        contentValues.put("Name",eventName);
        contentValues.put("Description",eventDesc);
        contentValues.put("StartTime",Long.toString(startDate.getTime()));
        contentValues.put("EndTime",Long.toString(endDate.getTime()));
        contentValues.put("Latitude",Float.toString(latitude));
        contentValues.put("Longitude",Float.toString(longitude));
        contentValues.put("Radius",Float.toString(radius));
        contentValues.put("Host",emailTrim);
        String query = "";
        for (Map.Entry e: contentValues.valueSet()) {
            query += (e.getKey() + "=" + e.getValue() + "&");
        }
        query = query.substring(0, query.length() - 1);
        final String fQuery = query;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }

}
