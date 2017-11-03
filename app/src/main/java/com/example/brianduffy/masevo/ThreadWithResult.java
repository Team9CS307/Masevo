package com.example.brianduffy.masevo;

import android.content.ContentValues;
import android.os.Build;
import android.support.annotation.RequiresApi;

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

public class ThreadWithResult extends Thread {
    private ResultSetter setter;
    public void setResultSetter(ResultSetter setter) {
        this.setter = setter;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {
        PublicEvent[] returnResult;
        String methodName;
        methodName = "getPublicEvents";
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

                ArrayList<PublicEvent> publicEventList = new ArrayList<PublicEvent>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                for (int i = 0; i < trtd.length; i++) {
                    Date d1, d2;
                    try {
                        d1 = (Date) sdf.parse(trtd[i][3]);
                        d2 = (Date) sdf.parse(trtd[i][4]);
                    } catch (ParseException pe) {
                        return;
                    }
                    PublicEvent p = new PublicEvent(trtd[i][1], trtd[i][2], d1, d2,
                            Float.parseFloat(trtd[i][5]), Float.parseFloat(trtd[i][6]),
                            Float.parseFloat(trtd[i][7]), trtd[i][8]);
                    publicEventList.add(p);
                }
                returnResult = publicEventList.toArray(new PublicEvent[publicEventList.size()]);
                setter.setResult(returnResult);
                // trtd now contains the desired array for this table
            }
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return;
        }
    }
}
