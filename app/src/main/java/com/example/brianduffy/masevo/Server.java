package com.example.brianduffy.masevo;

import android.os.StrictMode;

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
import java.util.Arrays;

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
    }

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

    public void createPublicEvent(int eventID, String eventName, String eventDescription,
                                  Date startDate, Date endDate,
                                  float latitude, float longitude,
                                  int radius, String [] compressedAttendeeList) {
        /*
        int hash = 1;
        hash = hash * 13 + eventName.hashCode();
        hash = hash * 17 + eventDescription.hashCode();
        hash = hash * 19 + startDate.hashCode();
        hash = hash * 23 + endDate.hashCode();
        hash = hash * 29 + (new Float(latitude)).hashCode();
        hash = hash * 31 + (new Float(longitude)).hashCode();
        hash = hash * 37 + radius;
        if (compressedAttendeeList != null) {
            hash = hash * 41 + compressedAttendeeList.hashCode();
        }
        int eventId = hash;
        System.out.println(eventId);*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection;
        try {
            System.out.println("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(url);
            System.out.println("Done.");
            System.out.println("Query data example:");
            System.out.println("=========================================");
            // Create and execute a SELECT SQL statement.
            PreparedStatement statement = connection.prepareStatement("INSERT INTO EventsTable " +
                    "(" + properties + ") " +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?)");

            Timestamp timestamp0 = new Timestamp(startDate.getTime());
            Timestamp timestamp1 = new Timestamp(endDate.getTime());

            statement.setInt(1, eventID);
            statement.setString(2, eventName);
            statement.setString(3, eventDescription);
            statement.setTimestamp(4, timestamp0);
            statement.setTimestamp(5, timestamp1);
            statement.setFloat(6, latitude);
            statement.setFloat(7, longitude);
            statement.setInt(8, radius);
            if (compressedAttendeeList != null) {
                statement.setString(9, Arrays.asList(compressedAttendeeList).toString());
            } else {
                statement.setString(9, "");
            }
            statement.executeUpdate();
            /*
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " +
                        resultSet.getString(2));
            }*/
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
