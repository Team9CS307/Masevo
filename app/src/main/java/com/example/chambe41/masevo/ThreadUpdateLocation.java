package com.example.chambe41.masevo;

import android.content.ContentValues;
import android.util.Pair;

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
import java.util.Map;

/**
 * Created by Brian Duffy on 12/1/2017.
 */

public class ThreadUpdateLocation implements Runnable {
    float latitude;
    float longitude;
    String email;
    Integer errno;
    private final String server_url = "http://webapp-171031005244.azurewebsites.net";

    Pair<Boolean,Integer> returnResult;
    public ThreadUpdateLocation(String email, float latitude, float longitude) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    @Override
    public void run() {

        String methodName;
        methodName = "getLocation";


        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
       contentValues.put("SenderEmail",email);
        contentValues.put("Latitude",Float.toString(latitude));
        contentValues.put("Longitude",Float.toString(longitude));

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
            //This will only run once, fool
            int count = 0;
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
                         if (errno!= 0){
                            returnResult = new Pair<>(false, errno);
                        } else {
                             returnResult = new Pair<>(true,errno);
                         }

                    }
                    count++;
            }

        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        // maybe change



    }

    public Pair<Boolean, Integer> getReturnResult() {

        return returnResult;
    }

}
