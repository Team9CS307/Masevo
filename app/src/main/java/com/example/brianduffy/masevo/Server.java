package com.example.brianduffy.masevo;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Server {
    private String hostName;
    private String dbName;
    private String admimName;
    private String adminPass;
    private String url;

    private final String properties = "eventID,eventName,eventDesc,startTime" +
            ",endTime,latitude,longitude,radius,hostActiveAttendUsers";

    public String getUrl() {
        return url;
    }

    public String insertToTable(String tableName, String [] columns, Object [] values) {
        String query = "";
        return query;
    }
    /*
    public Server(String hostName, String dbName, String admimName, String adminPass) {
        this.hostName = hostName;
        this.dbName = dbName;
        this.admimName = admimName;
        this.adminPass = adminPass;
        this.url = String.format("jdbc:jtds:sqlserver://%s:1433/" +
                "%s;" +
                "user=%s@masevo;" +
                "password=%s;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;", hostName, dbName, admimName, adminPass);
    }*/

    public void getPublicEvent(int eventID) {
        String strEventID = Integer.toString(eventID);
        String query = "SELECT " + properties +
                " FROM EventsTable" +
                " where eventID = '" + strEventID + "';";
        Connection connection;
        Statement statement = null;
        try {
            System.out.println("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(url);
            System.out.println("Done.");
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String output = String.format("%s\n%s\n%s\n%s\n" +
                                "%s\n%s\n%s\n%s",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9));
                System.out.println(output);
            }
            connection.close();
        } catch (SQLTimeoutException sqlte) {
            System.out.println("Could not execute the query within the timeout value specified" +
                    " by setQueryTimeout.");
            sqlte.printStackTrace();

        } catch (SQLException sqle) {
            System.out.println("Could not create statement in connection. Either the " +
                    "connection is already closed or access was denied to the database.");
            sqle.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPublicEvent(int ID, String eventName, String eventDescription,
                                  Date startDate, Date endDate,
                                  float latitude, float longitude,
                                  float radius, String email) {
        String methodName = "createPublicEvent";
        String emailTrim = email.substring(0,email.indexOf("@"));
        String postNoXML = String.format("method=%s&" +
                        "ID=%s&" +
                        "Name=%s&" +
                        "Description=%s&" +
                        "StartTime=%s&" +
                        "EndTime=%s&" +
                        "Latitude=%s&" +
                        "Longitude=%s&" +
                        "Radius=%s&" +
                        "Host=%s&", methodName, ID, eventName, eventDescription, startDate.getTime(), endDate.getTime(),
                latitude, longitude, radius, emailTrim);

            ContentValues contentValues = new ContentValues();
            contentValues.put("method",methodName);
            contentValues.put("ID",Integer.toString(ID));
            contentValues.put("Name",eventName);
            contentValues.put("Description",eventDescription);
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
                @Override
                public void run() {
                    try {
                        byte[] postData = fQuery.getBytes(StandardCharsets.UTF_8);
                        int postDataLength = postData.length;
                        URL url = new URL("http://webapp-171031005244.azurewebsites.net");
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
        /*
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://webapp-171031005244.azurewebsites.net");
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("method",methodName));
        arguments.add(new BasicNameValuePair("ID",Integer.toString(ID)));
        arguments.add(new BasicNameValuePair("Name",eventName));
        arguments.add(new BasicNameValuePair("Description",eventDescription));
        arguments.add(new BasicNameValuePair("StartTime",Long.toString(startDate.getTime())));
        arguments.add(new BasicNameValuePair("EndTime",Long.toString(endDate.getTime())));
        arguments.add(new BasicNameValuePair("Latitude",Float.toString(latitude)));
        arguments.add(new BasicNameValuePair("Longitude",Float.toString(longitude)));
        arguments.add(new BasicNameValuePair("Radius",Float.toString(radius)));
        arguments.add(new BasicNameValuePair("Host",emailTrim));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }*/
    }

    public void joinEvent(int eventID) {
        String strEventID = Integer.toString(eventID);
        String query = "SELECT " + properties +
                " FROM EventsTable" +
                " where eventID = '" + strEventID + "';";
        Connection connection;
        Statement statement = null;
        try {
            System.out.println("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(url);
            System.out.println("Done.");
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String output = String.format("%s\n%s\n%s\n%s\n" +
                                "%s\n%s\n%s\n%s",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9));
                System.out.println(output);
            }
            connection.close();
        } catch (SQLTimeoutException sqlte) {
            System.out.println("Could not execute the query within the timeout value specified" +
                    " by setQueryTimeout.");
            sqlte.printStackTrace();

        } catch (SQLException sqle) {
            System.out.println("Could not create statement in connection. Either the " +
                    "connection is already closed or access was denied to the database.");
            sqle.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public static void main(String[] args) {
        String hostName = "masevo.database.windows.net";
        String dbName = "MasevoFields2";
        String user = "MASEVO_ADMIN";
        String password = "M4s3v0_4dm1n";
        Server server = new Server(hostName, dbName, user, password);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        String d1 = "2017-10-05 16:00";
        String d2 = "2017-10-05 17:00";
        Date startDate = null;
        Date endDate = null;
        try {
            java.util.Date jud1 = sdf.parse(d1);
            java.util.Date jud2 = sdf.parse(d2);
            startDate = new Date(jud1.getTime());
            endDate = new Date(jud2.getTime());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        server.createPublicEvent("event2", "test event", startDate, endDate, 40.4302296f, -86.9107470f, 5, null);
        //server.getPublicEvent(1524714392);
    }
    */
}
