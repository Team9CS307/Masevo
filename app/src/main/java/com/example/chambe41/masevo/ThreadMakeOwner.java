package com.example.chambe41.masevo;

import android.content.ContentValues;
import android.util.Pair;

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
import java.util.Map;

/**
 * Created by Brian Duffy on 12/1/2017.
 */

public class ThreadMakeOwner implements Runnable {
    String SenderEmail;
    String targetEmail;
    int eventID;
    Boolean isPub;
    int priv;
    private Integer errno;

    public ThreadMakeOwner(int eventID,String senderEmail, String targetEmail, Boolean isPub) {
        SenderEmail = senderEmail;
        this.targetEmail = targetEmail;
        this.eventID = eventID;
        this.priv = priv;
        this.isPub = isPub;
    }

    private final String server_url = "http://webapp-171031005244.azurewebsites.net";
    private Pair<Boolean, Integer> returnResult;

    @Override
    public void run() {
        String methodName = "makeOwner";
        ContentValues contentValues = new ContentValues();
        contentValues.put("method",methodName);
        contentValues.put("ID",Integer.toString(eventID));
        contentValues.put("SenderEmail",SenderEmail);
        contentValues.put("TargetEmail",targetEmail);
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
                errno = Integer.parseInt(trtd[0][0]);
                if (errno != 0) {
                    returnResult = new Pair<>(false,errno);
                }else {
                    returnResult = new Pair<>(true,errno);
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
    public Pair<Boolean, Integer> getReturnResult() {
        return returnResult;
    }
}
