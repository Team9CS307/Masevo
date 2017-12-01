package com.example.brianduffy.masevo;

import com.example.chambe41.masevo.*;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event implements Serializable{
    // Use this to create a PublicEvent
    private final String properties = "ID,Name,Description,StartTime,EndTime," +
            "Latitude,Longitude,Radius,Host,List";

    private final String server_url = "http://webapp-171031005244.azurewebsites.net";

    /**
     * Used for call to server which do not require an event to be present
     */
    public PublicEvent() {};
    public PublicEvent(String eventName, String eventDesc, Date startDate, Date endDate,
                       float latitude, float longitude, float radius, String creatorEmail)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = new Location(latitude, longitude);
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.radius = radius;
        this.hostEmail = creatorEmail;

        this.hostList = new HashSet<>();
        this.attendeeList = new HashSet<>();
        this.activeList = new HashSet<>();
        this.emailToDisplay = new HashMap<>();

        this.eventID = (eventName + eventDesc + startDate + endDate + latitude + longitude +
                radius + Double.toString(Math.random())).hashCode();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Pair<Event, Integer> createEvent(String eventName,String eventDesc,Date startDate,Date endDate,
                Location location, float radius,int eventID, String hostEmail,boolean pub) {
        ThreadCreateEvent threadCreateEvent = new ThreadCreateEvent(eventName,eventDesc,startDate,
                endDate,location,radius,eventID,hostEmail,true);
        Thread thread = new Thread(threadCreateEvent);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return threadCreateEvent.getReturnResult();
    }
    @Override
    public Pair<ArrayList<? extends Event>, Integer> getEvents() {
        ThreadGetEvents threadGetEvents = new ThreadGetEvents();
        new Thread(threadGetEvents).start();
        return threadGetEvents.getReturnResult();
    }
    @Override
    public Pair<ArrayList<? extends Event>, Integer> getMyEvents(String email) {
        ThreadGetUserEvents myEvents = new ThreadGetUserEvents(email);
        new Thread(myEvents).start();
        return myEvents.getReturnResult();
    }

    @Override
    public Pair<Boolean,Integer> deleteEvent(int eventID, String email) {
        ThreadDeleteEvent threadDeleteEvent = new ThreadDeleteEvent(eventID,email,true);
        new Thread(threadDeleteEvent).start();


        return threadDeleteEvent.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> removeFromActive(int eventID, String email) {
        ThreadRemoveFromActive threadRemoveFromActive = new ThreadRemoveFromActive(eventID,email,true);
        new Thread(threadRemoveFromActive).start();
        return threadRemoveFromActive.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> addToActive(int eventID, String email) {
        ThreadAddToActive threadAddToActive = new ThreadAddToActive(eventID,email,true);
        new Thread(threadAddToActive).start();
        return threadAddToActive.getReturnResult();
    }

    @Override
    public Pair<Boolean,Integer> joinEvent(int eventID,String senderEmail) {
        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(eventID,senderEmail,true);
        new Thread(threadJoinEvent).start();
        return threadJoinEvent.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> leaveEvent(int eventID, String senderEmail) {
        ThreadLeaveEvent threadLeaveEvent = new ThreadLeaveEvent(eventID, senderEmail, true);
        new Thread(threadLeaveEvent).start();
        return threadLeaveEvent.getReturnResult();
    }

    @Override
    public Pair<Boolean,Integer> addUser (int eventID, String email,String target) {
    ThreadAddUser threadAddUser = new ThreadAddUser(eventID,email,target,true);
        new Thread(threadAddUser).start();
        return threadAddUser.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> removeUser(int eventID, String email,String target) {
        ThreadRemoveUser threadRemoveUser = new ThreadRemoveUser(eventID,email,target,true);
        new Thread(threadRemoveUser).start();
        return threadRemoveUser.getReturnResult();

    }
    @Override
    public Pair<Boolean, Integer> banUser(int eventID, String email, String target) {
        ThreadBanUser threadBanUser = new ThreadBanUser(eventID,email,target,true);
        new Thread(threadBanUser).start();
        return threadBanUser.getReturnResult();

    }
    @Override
    public Pair<Boolean, Integer> makeOwner(int eventID, String email, String target) {
        ThreadMakeOwner threadMakeOwner = new ThreadMakeOwner(eventID,email,target,true);
        new Thread(threadMakeOwner).start();
        return threadMakeOwner.getReturnResult();

    }
    @Override
    public Pair<Boolean, Integer> makeHost(int eventID, String email, String target) {
        ThreadMakeHost threadMakeHost = new ThreadMakeHost(eventID,email,target,true);
        new Thread(threadMakeHost).start();
        return threadMakeHost.getReturnResult();

    }
    @Override
    public Pair<Boolean, Integer> makeUser(int eventID, String email, String target) {
        ThreadMakeUser threadMakeUser = new ThreadMakeUser(eventID,email,target,true);
        new Thread(threadMakeUser).start();

        return threadMakeUser.getReturnResult();
    }

    @Override
    public Pair<Event, Integer> modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate,
                                            float latitude, float longitude, float radius, String hostEmail) {
        ThreadModifyEvent threadModifyEvent = new ThreadModifyEvent(eventID,eventName,eventDesc,
                startDate,endDate,latitude,longitude,radius,hostEmail,true);
        new Thread(threadModifyEvent).start();
        return threadModifyEvent.getReturnResult();
    }

}
