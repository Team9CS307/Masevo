package com.example.chambe41.masevo;

import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import com.example.brianduffy.masevo.DatePickerFragment;
import com.example.brianduffy.masevo.Event;
import com.example.brianduffy.masevo.Location;
import com.example.brianduffy.masevo.PrivateEvent;
import com.example.brianduffy.masevo.PublicEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    java.sql.Date startDate;
    java.sql.Date endDate;
    String hostEmail;
    int eventID;
    String eventName;
    String eventDesc;
    Location location;
    float radius;
    private Pair<? extends Event, Integer> returnResult;
    Integer errno;
    public ThreadCreateEvent(String eventName,String eventDesc,java.sql.Date startDate,java.sql.Date endDate,
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        //TODO maybe?
        String methodName;
            methodName = "createEvent";

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
        contentValues.put("SenderEmail",hostEmail);
        contentValues.put("isPub",pub);

        //TODO create the actual event

        String query = "";
        for (Map.Entry e: contentValues.valueSet()) {
            query += (e.getKey() + "=" + e.getValue() + "&");
        }
        query = query.substring(0, query.length() - 1);
        final String fQuery = query;
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
            String result = "";
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                String output;
                while ((output = br.readLine()) != null) {
                    result += output;
                }
                System.out.println("Response message: " + result);
            }
            Document doc = Jsoup.parse(result);
            Elements tables = doc.select("table");
            int count = 0;
            //This will only run once, fool
            for (Element table : tables) {
                Elements trs = table.select("tr");
                String[][] trtd = new String[trs.size()][];
                for (int i = 0; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("td");
                    trtd[i] = new String[tds.size()];
                    for (int j = 0; j < tds.size(); j++) {
                        trtd[i][j] = tds.get(j).text();
                    }
                }
                if (count == 0) {
                    errno = Integer.parseInt(trtd[0][0]);
                } else {
                    if (pub) {
                        returnResult = new Pair<>(new PublicEvent(eventName,eventDesc,
                                startDate,endDate,location.latitude,location.longitude,radius,hostEmail),errno);
                    } else {
                        returnResult = new Pair<>(new PrivateEvent(eventName,eventDesc,
                                startDate,endDate,location.latitude,location.longitude,radius,hostEmail),errno);
                    }
                }
            }

        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }



    }

    public Pair<? extends Event, Integer> getReturnResult() {
        System.out.println(errno);
        return returnResult;
    }
}
