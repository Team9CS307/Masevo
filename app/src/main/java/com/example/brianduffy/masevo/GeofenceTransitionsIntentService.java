package com.example.brianduffy.masevo;

/**
 * Created by Brian Duffy on 11/20/2017.
 */

import static android.content.ContentValues.TAG;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens for geofence transition changes.
 */
public class GeofenceTransitionsIntentService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     * Services (inside a PendingIntent) when addGeofences() is called.
     */
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {

            System.err.println("error");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            android.location.Location l = geofencingEvent.getTriggeringLocation();
            for (int i = 0; i < MainActivity.user.myevents.size(); i++) {
                android.location.Location a = new Location("cancer");
                a.setLatitude(MainActivity.location.getLatitude());
                a.setLongitude(MainActivity.location.getLongitude());
                Event event = MainActivity.user.myevents.get(i);
                Boolean isPub = false;
                if (event instanceof PublicEvent) {
                    isPub = true;
                }
                if (l.distanceTo(a) < event.radius) {
                    ThreadAddToActive addToActive = new ThreadAddToActive(event.eventID, MainActivity.user.emailAddress, isPub);
                    Thread thread = new Thread(addToActive);
                    thread.start();
                    try {
                        thread.join();
                        Pair<Boolean,Integer> ret =addToActive.getReturnResult();

                        if (ret.second != 0) {
                            Toast.makeText(getApplicationContext(), Error.getErrorMessage(ret.second), Toast.LENGTH_SHORT).show();
                        } else {
                            //TODO shit pants if fekin worked
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } else {
                    ThreadRemoveFromActive removeFromActive = new ThreadRemoveFromActive(event.eventID,
                            MainActivity.user.emailAddress,isPub);
                    Thread t1 = new Thread(removeFromActive);
                    t1.start();
                    try {
                        t1.join();
                        Pair<Boolean,Integer> ret =removeFromActive.getReturnResult();

                        if (ret.second != 0) {
                            Toast.makeText(getApplicationContext(), Error.getErrorMessage(ret.second), Toast.LENGTH_SHORT).show();
                        } else {
                            //TODO shit pants if fekin worked
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Get the transition details as a String.


            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.

            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "issue");
        }
    }
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        int count = 0;
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());

            count++;
        }
        int i = 0;
        for (Geofence geo : MainActivity.mGeofenceList) {
            if (geo.getRequestId().equals(MainActivity.mGeofenceList.get(i).getRequestId())) {
                System.out.println("wieo2jwgopierg;ewrjgi");
            }

                i++;
        }

        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exit";
            default:
                return "Error";
        }
    }

    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_masevo_app)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.

                .setContentTitle(notificationDetails)
                .setContentText(notificationDetails.substring(0,notificationDetails.indexOf(":")))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Showing a toast message, using the Main thread
     */
    private void showToast(final Context context, final int resourceId) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "entered a place kofwnfwo", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("hello");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        System.out.println("hello");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("hello");
    }

}