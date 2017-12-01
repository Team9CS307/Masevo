package com.example.brianduffy.masevo;

import android.widget.Toast;

/**
 * Created by Brian Duffy on 12/1/2017.
 */

public class Error {
    Integer errno;

   public static String getErrorMessage(Integer errno) {
       String errmessage = "";
       switch (errno) {
           case 1:
               errmessage = "EVENT ID NOT FOUND ERROR\n";
               break;
           case 2:
               errmessage = "WRONG PERMISSIONS ERROR\n";
               break;
           case 3:
               errmessage = "SET PUBLIC/PRIVATE ERROR\n";
               break;
           case 4:
               errmessage = "NOT IN LIST ERROR\n";
               break;
           case 5:
               errmessage = "ATTEMPT TO ADD A BANNED USER ERROR";
               break;
           case 6:
               errmessage = " BANNED\n";
               break;
           case 7:
               errmessage = "ALREADY IN EVENT\n";
               break;
           case 8:
               errmessage = "NOT IN EVENT\n";
               break;
           case 9:
               errmessage = "NOT OWNER\n";
               break;
           case 10:
               errmessage = "LEAVE AS OWNER";
               break;
           case 11:
               errmessage = "SELF ERROR (remove, add)\n";
               break;
       }
       return errmessage;
   }

}
