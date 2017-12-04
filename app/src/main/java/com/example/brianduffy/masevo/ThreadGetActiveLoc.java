package com.example.brianduffy.masevo;

import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import com.example.brianduffy.masevo.*;

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
import java.util.Map;

/**
 * Created by Brian Duffy on 12/1/2017.
 */

public class ThreadGetActiveLoc implements Runnable {
    int eventID;
    String SenderEmail;
    Boolean isPub;
    Integer errno;
    ArrayList<String> names = new ArrayList<>();

    int userPriv;
    ArrayList<Location> locations = new ArrayList<>();
    Pair<ArrayList<Location>,ArrayList<String>> returnResult;
    private final String server_url = "http://webapp-171031005244.azurewebsites.net";
    String TargetEmail;
    public ThreadGetActiveLoc(int eventID, String SenderEmail,Boolean isPub) {
        this.eventID = eventID;
        this.SenderEmail = SenderEmail;
        this.isPub = isPub;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        String methodName = "getActiveLoc";
        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
        contentValues.put("ID",Integer.toString(eventID));
        contentValues.put("isPub",isPub);


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
                while((output = br.readLine()) != null)
                {
                    result += output;
                }
                System.out.println("Response message: " + result);
            }
            Document doc = Jsoup.parse(result);
            Elements tables = doc.select("table");
            //This will only run once, fool
            //TODO do this
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

                if (count == 0)  {
                    errno = Integer.parseInt(trtd[0][0]);
                } else {
                    for (int i = 0; i < trtd.length; i++) {
                        Location loc = new Location(0,0);
                        for (int j = 0; j < trtd[i].length; j++) {
                            if (j == 0) {
                                loc.latitude = Float.parseFloat(trtd[i][j]);
                            } else if (j == 1){
                                loc.longitude = Float.parseFloat(trtd[i][j]);
                            } else {
                                names.add(trtd[i][j]);
                            }
                        }
                        locations.add(loc);
                    }
                }
                count++;
            }

            returnResult = new Pair<>(locations,names);

        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }


    }
    public Pair<ArrayList<Location>, ArrayList<String>> getReturnResult() {
        return returnResult;
    }
}

