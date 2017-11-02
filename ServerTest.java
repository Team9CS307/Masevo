//import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
//import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.net.URL;

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


public class ServerTest {

    private URL masevoURL;
    private HttpURLConnection masevoWebsite;
    private String testParams;

    /*
        Constructor connects to website
     */
    public ServerTest() throws Exception {
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
    private String getHtmlString(ServerTest st) throws IOException {

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





    //TEST 1 - Single Post Request

    @Test
    public void test1_OnePost() throws Exception {
        ServerTest st1 = new ServerTest();
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
        ServerTest st2 = new ServerTest();

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
        ServerTest st3 = new ServerTest();
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
        ServerTest st4 = new ServerTest();
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
        ServerTest st5 = new ServerTest();
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
        ServerTest st6 = new ServerTest();
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
        ServerTest st7 = new ServerTest();
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
        ServerTest st8 = new ServerTest();
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
        ServerTest st9 = new ServerTest();
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
        ServerTest st10 = new ServerTest();
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
        ServerTest st11 = new ServerTest();
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
        ServerTest st12 = new ServerTest();
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
        ServerTest st13 = new ServerTest();
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
        ServerTest st14 = new ServerTest();
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
        ServerTest st15 = new ServerTest();
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
        ServerTest st16 = new ServerTest();
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










}
