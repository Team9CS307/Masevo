package com.example.chambe41.masevo;

import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Josh on 11/3/2017.
 */

public class ThreadGetEvents implements Runnable {
    private ArrayList<PublicEvent> returnResult = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        String methodName;
        methodName = "getEvents";
        ContentValues contentValues = new ContentValues();
        contentValues.put("method", methodName);
        String query = "";
        for (Map.Entry e : contentValues.valueSet()) {
            query += (e.getKey() + "=" + e.getValue() + "&");
        }
        query = query.substring(0, query.length() - 1);
        final String fQuery = query;
        try {
            byte[] postData = fQuery.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL("http://webapp-171031005244.azurewebsites.net");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                for (String[] aTrtd : trtd) {
                    java.util.Date d1, d2;
                    Date d3, d4;
                    try {
                        d1 = sdf.parse(aTrtd[3]);
                        d2 = sdf.parse(aTrtd[4]);
                        d3 = new Date(d1.getTime());
                        d4 = new Date(d2.getTime());
                    } catch (ParseException pe) {
                        return;
                    }
                    PublicEvent p = new PublicEvent(aTrtd[1], aTrtd[2], d3, d4,
                            Float.parseFloat(aTrtd[5]), Float.parseFloat(aTrtd[6]),
                            Float.parseFloat(aTrtd[7]), aTrtd[8]);
                    returnResult.add(p);
                }
            }
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    public ArrayList<PublicEvent> getReturnResult() {
        return returnResult;
    }
}
