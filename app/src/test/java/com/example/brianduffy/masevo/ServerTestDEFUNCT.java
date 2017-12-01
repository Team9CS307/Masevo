package com.example.brianduffy.masevo;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.Assert;
import org.junit.Test;
//import org.jsoup.nodes.Document;

import java.io.*;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* ***************************************************
 * THESE TESTS ARE DEFUNCT, KEPT ONLY FOR ARCHIVE
 * **************************************************
 * */






/*
* Masevo Server Test Cases
*
* @author Vikram Pasumarti, vpasuma@purdue.edu
* @version 2 November 2017
*
*/



/*
COL NAME	TYPE		NULL?
-------------------------------------
ID		    int		    no
Name		nchar(24)	no
Description	nchar(300)	yes
StartTime	datetime	no
EndTime		datetime	no
Latitude	float		no
Longitude	float		no
Radius		float		no
Host		nchar(64)	no
List		xml		    no
 */

/*
VALID INPUTS
------------
0-9
a-z
A-Z
$-_.!*'(),"
+ means space
 */


public class ServerTestDEFUNCT {

    private URL masevoURL;
    private HttpURLConnection masevoWebsite;
    private String testParams;

    /*
        Constructor connects to website
     */
    public ServerTestDEFUNCT() throws Exception {
        masevoURL = new URL("http://webapp-171031005244.azurewebsites.net");
        masevoWebsite = (HttpURLConnection) masevoURL.openConnection();
        masevoWebsite.setRequestMethod("POST");
        masevoWebsite.setRequestProperty("User-Agent", "Mozilla/5.0");
    }




    //    -------------------------------------------------------------------------
//    METHOD TO GET HTML TO BE PARSED, ORIGINALLY PLANNED TO BE USED WITH JSOUP
//    NO LONGER NEEDED BECAUSE COMPARING HTML RESPONSES
//    Currently being used for debugging
//    -------------------------------------------------------------------------
    private String getHtmlString(ServerTestDEFUNCT st) throws IOException {

        InputStreamReader isr = new InputStreamReader(st.masevoWebsite.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String linesFromWebsite;
        String html = "";

        while ((linesFromWebsite = br.readLine()) != null) {
            html = html + linesFromWebsite;
        }
        br.close();
        isr.close();
        return html;
    }

// <Users>
//        <User>
//            <Name></Name>
//            <Permission></Permission>
//            <Active></Active>
//         </User>
// </Users>



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String backFromDatabase()  {
        String dbOutput = "";
        String hostName = "masevo.database.windows.net";
        String dbName = "masevo";
        String user = "masevo_admin";
        String password = "M4s3v0_4dm1n";
        String url = String.format("jdbc:jtds:sqlserver://%s:1433;database=%s;user=%s@masevo;password=%s;" +
                "encrypt=true;trustServerCertificate=false;hostNameIn" +
                "Certificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            //String schema = connection.getSchema();
            //System.out.println("Successful connection - Schema: " + schema);

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM PublicEvents";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement

                while (resultSet.next())
                {
                    dbOutput = resultSet.getString(1) + " "
                            + resultSet.getString(2) + " "
                            + resultSet.getString(3) + " "
                            + resultSet.getString(4) + " "
                            + resultSet.getString(5) + " "
                            + resultSet.getString(6) + " "
                            + resultSet.getString(7) + " "
                            + resultSet.getString(8) + " "
                            + resultSet.getString(9) + " "
                            + resultSet.getString(10) + " ";
                    System.out.println(dbOutput);

                }
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return dbOutput;
    }




    //TEST 1 - Single Post Request

    @Test
    public void test1_OnePost() throws Exception {
        ServerTestDEFUNCT st1 = new ServerTestDEFUNCT();
        st1.testParams = "ID=1";
        st1.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st1.masevoWebsite.getOutputStream());
        dos.writeBytes(st1.testParams);
        dos.flush();
        dos.close();
        int postResponse = st1.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st1); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,postResponse);
    }


    //TEST 2 - Large Post Request

    @Test
    public void test2_LargePost() throws Exception {
        ServerTestDEFUNCT st2 = new ServerTestDEFUNCT();

        st2.testParams = "ID=0";

        for (int i = 1; i <= 10000; i++) {
            st2.testParams = st2.testParams + "&ID="+i;
        }
        for (int i = 0; i <= 10000; i++) {
            st2.testParams = st2.testParams + "&Name="+"Vik"+i;
        }
        for (int i = 0; i <= 10000; i++) {
            st2.testParams = st2.testParams + "&Description="+"This is a description"+i;
        }
        st2.testParams = st2.testParams + "&Description=This is the last description";

        st2.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st2.masevoWebsite.getOutputStream());
        dos.writeBytes(st2.testParams);
        dos.flush();
        dos.close();
        int pushResponse = st2.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st2); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,pushResponse);
    }


    //TEST 3 - Nullable values (NOTE: "Description" CAN be null)

    @Test
    public void test3_NullValuePost() throws Exception {
        ServerTestDEFUNCT st3 = new ServerTestDEFUNCT();
        st3.testParams = "ID="+null+"&Name="+null+"&StartTime="+null+"&EndTime="+null+"&Latitude="+null+"&Longitude="+null+
                "&Radius="+null+"&Host="+null+"&List="+null;
        st3.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st3.masevoWebsite.getOutputStream());
        dos.writeBytes(st3.testParams);
        dos.flush();
        dos.close();
        int postResponse = st3.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st3); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 4 - Description Null value (NOTE: "Description" CAN be null)

    @Test
    public void test4_DescNullValuePost() throws Exception {
        ServerTestDEFUNCT st4 = new ServerTestDEFUNCT();
        st4.testParams = "Description="+null;
        st4.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st4.masevoWebsite.getOutputStream());
        dos.writeBytes(st4.testParams);
        dos.flush();
        dos.close();
        int postResponse = st4.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st4); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,postResponse);
    }

    //TEST 5 - Name Length

    @Test
    public void test5_NameLengthPost() throws Exception {
        ServerTestDEFUNCT st5 = new ServerTestDEFUNCT();
        st5.testParams = "Name=ThisNameisLonger+Than24Characters";
        st5.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st5.masevoWebsite.getOutputStream());
        dos.writeBytes(st5.testParams);
        dos.flush();
        dos.close();
        int postResponse = st5.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st5); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 6 - Description Length

    @Test
    public void test6_DescLengthPost() throws Exception {
        ServerTestDEFUNCT st6 = new ServerTestDEFUNCT();
        st6.testParams = "Description=ThisDescriptionIsOver300CharacterThisDescriptionIsOver300Character" +
                "ThisDescriptionIsOver300CharacterThisDescriptionIsOver300CharacterThisDescriptionIsOver300Character" +
                "ThisDescriptionIsOver300CharacterThisDescriptionIsOver300CharacterThisDescriptionIsOver300Character" +
                "ThisDescriptionIsOver300CharacterThisDescriptionIsOver300Characters";
        st6.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st6.masevoWebsite.getOutputStream());
        dos.writeBytes(st6.testParams);
        dos.flush();
        dos.close();
        int postResponse = st6.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st6); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 7 - Host Length

    @Test
    public void test7_HostLengthPost() throws Exception {
        ServerTestDEFUNCT st7 = new ServerTestDEFUNCT();
        st7.testParams = "Host=ThisHostisLonger+Than64CharactersThisHostisLonger+Than64Characters";
        st7.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st7.masevoWebsite.getOutputStream());
        dos.writeBytes(st7.testParams);
        dos.flush();
        dos.close();
        int postResponse = st7.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st7); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 8 - Correct Name Length

    @Test
    public void test8_CorrectNameLengthPost() throws Exception {
        ServerTestDEFUNCT st8 = new ServerTestDEFUNCT();
        st8.testParams = "Name=This+NameIs+24Characters";
        st8.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st8.masevoWebsite.getOutputStream());
        dos.writeBytes(st8.testParams);
        dos.flush();
        dos.close();
        int postResponse = st8.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st8); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,postResponse);
    }

    //TEST 9 - Correct Description Length

    @Test
    public void test9_CorrectDescLengthPost() throws Exception {
        ServerTestDEFUNCT st9 = new ServerTestDEFUNCT();
        st9.testParams = "Description=ThisDescriptionIs300CharactersThisDescriptionIs300CharactersThisDescription" +
                "Is300CharactersThisDescriptionIs300CharactersThisDescriptionIs300CharactersThisDescriptionIs300" +
                "CharactersThisDescriptionIs300CharactersThisDescriptionIs300CharactersThisDescriptionIs300Characters" +
                "ThisDescriptionIs300Characters";
        st9.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st9.masevoWebsite.getOutputStream());
        dos.writeBytes(st9.testParams);
        dos.flush();
        dos.close();
        int postResponse = st9.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st9); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,postResponse);
    }

    //TEST 10 - Correct Host Length

    @Test
    public void test10_CorrectHostLengthPost() throws Exception {
        ServerTestDEFUNCT st10 = new ServerTestDEFUNCT();
        st10.testParams = "Host=ThisHostIs+64Characters+ThisHostIs+64Characters+ThisHostIs64Char";
        st10.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st10.masevoWebsite.getOutputStream());
        dos.writeBytes(st10.testParams);
        dos.flush();
        dos.close();
        int postResponse = st10.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st10); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(200,postResponse);
    }

    //TEST 11 - Verify ID int Type

    @Test
    public void test11_VerifyIDintTypePost() throws Exception {
        ServerTestDEFUNCT st11 = new ServerTestDEFUNCT();
        double IDnum = 1.5;
        st11.testParams = "ID="+IDnum;
        st11.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st11.masevoWebsite.getOutputStream());
        dos.writeBytes(st11.testParams);
        dos.flush();
        dos.close();
        int postResponse = st11.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st11); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 12 - Verify ID Type Not A Number

    @Test
    public void test12_VerifyIDintTypeNotANumberPost() throws Exception {
        ServerTestDEFUNCT st12 = new ServerTestDEFUNCT();
        String IDnum = "this+is+not+a+number";
        st12.testParams = "ID="+IDnum;
        st12.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st12.masevoWebsite.getOutputStream());
        dos.writeBytes(st12.testParams);
        dos.flush();
        dos.close();
        int postResponse = st12.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st12); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 13 - Verify start time is a long

    @Test
    public void test13_VerifyLongStartTimePost() throws Exception {
        ServerTestDEFUNCT st13 = new ServerTestDEFUNCT();
        BigInteger notALong = new BigInteger("9223372036854775808");
        st13.testParams = "StartTime=" + notALong;
        st13.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st13.masevoWebsite.getOutputStream());
        dos.writeBytes(st13.testParams);
        dos.flush();
        dos.close();
        int postResponse = st13.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st13); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 14 - Verify end time is a long

    @Test
    public void test14_VerifyLongEndTimePost() throws Exception {
        ServerTestDEFUNCT st14 = new ServerTestDEFUNCT();
        BigInteger notALong = new BigInteger("9223372036854775808");
        st14.testParams = "EndTime=" + notALong;
        st14.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st14.masevoWebsite.getOutputStream());
        dos.writeBytes(st14.testParams);
        dos.flush();
        dos.close();
        int postResponse = st14.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st14); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 15 - Verify latitude, longitude, and radius are float

    @Test
    public void test15_VerifyFloatValuesPost() throws Exception {
        ServerTestDEFUNCT st15 = new ServerTestDEFUNCT();
        double maxDouble = Double.MAX_VALUE;
        st15.testParams = "Longitude=" + maxDouble + "&Latitude=" + maxDouble +"&Radius=" + maxDouble;
        st15.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st15.masevoWebsite.getOutputStream());
        dos.writeBytes(st15.testParams);
        dos.flush();
        dos.close();
        int postResponse = st15.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st15); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 16 - Verify correct input characters
    //$-_.!*'(),"
    @Test
    public void test16_VerifyInputCharsPost() throws Exception {
        ServerTestDEFUNCT st16 = new ServerTestDEFUNCT();
        String invalidCharacters = "@#%^&=\\|[]<>?/`~:;{}";
        st16.testParams = "Name="+invalidCharacters;
        st16.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st16.masevoWebsite.getOutputStream());
        dos.writeBytes(st16.testParams);
        dos.flush();
        dos.close();
        int postResponse = st16.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st16); //kept this for debugging
        System.out.println(returnedHtml);

        Assert.assertEquals(400,postResponse);
    }

    //TEST 17 - Verify Database Server communication
    //$-_.!*'(),"
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void test17_VerifyDatabaseServerCommunication() throws Exception {
        ServerTestDEFUNCT st17 = new ServerTestDEFUNCT();
        String xmlString = "<Users><User><Name>NameOfUser</Name><Permission>1</Permission><Active>1</Active></User></Users>";
        int ID = 0;
        String name = "NameofEvent";
        String hostName = "NameofTheHost";
        String description = "Thisisanevent";
        long longStartTime;
        long longEndTime;

        String str_date1="2017-11-02 12:00:00.000";
        String str_date2="2017-11-03 12:00:00.000";
        DateFormat formatter ;
        Date date1 ;
        Date date2;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm");
        date1 = (Date)formatter.parse(str_date1);
        date2 = (Date)formatter.parse(str_date2);

        longStartTime = date1.getTime();
        longEndTime = date2.getTime();

        float longitude = 0;
        float latitude = 0;
        float radius = 0;



        st17.testParams = "ID="+ ID + "&Name=" + name + "&Description=" + description + "&StartTime=" + longStartTime +
                "&EndTime=" + longEndTime + "&Latitude=" + latitude + "&Longitude=" + longitude + "&Radius=" + radius +
                "&Host=" + hostName + "&List=" + xmlString;


        st17.masevoWebsite.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(st17.masevoWebsite.getOutputStream());
        dos.writeBytes(st17.testParams);
        dos.flush();
        dos.close();
        int postResponse = st17.masevoWebsite.getResponseCode();

        String returnedHtml = getHtmlString(st17); //kept this for debugging
        System.out.println(returnedHtml);
        System.out.println(postResponse);

        String valuesSent = "0 NameofEvent Thisisanevent " + str_date1 + " " + str_date2 + " 0 0 0 NameofTheHost " +
                "<Users><User><Name>NameOfUser</Name><Permission>1</Permission><Active>1</Active></User></Users>";
        String valuesReturned = st17.backFromDatabase();
        Assert.assertEquals(valuesSent,valuesReturned);
    }
}