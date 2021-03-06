package com.example.brianduffy.masevo;

import android.util.Pair;

import com.example.chambe41.masevo.ThreadUpdateLocation;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public class User {
    String emailAddress;
    //HashMap<Integer, String> myEventList; // Why did we have a HashMap here not a HashSet?
    ArrayList<Integer> myPrivateEventIDs;
    ArrayList<Integer> myPublicEventIDs;
    ArrayList<PublicEvent> myPublicEventList;
    ArrayList<PrivateEvent> myPrivateEventList;
    ArrayList<Event> myevents = new ArrayList<>();
    Location myLocation;
    ArrayList<PublicEvent> events = new ArrayList<>();
    ArrayList<Event> nearby = new ArrayList<>();
    ArrayList<Integer> myIDs = new ArrayList<>();

    // TODO instead of using HashSets, use Arraylists. It is much easier to use with the listview fragments

    // We decided that Brian would store the ID of events that users are subscribed to in a .txt
    // locally. So I am going to assume here that I will be passed 2 HashSest of ints containing IDs
    // One set will be from the .txt of public, the other set from the .txt of private
    // Upon quiting the app the user should write these IDS to the .txt files
    public User(String emailAddress, float longitude, float latitude, ArrayList<Integer> publicIDs,
                ArrayList<Integer> privateIDs)
    {
        this.emailAddress = emailAddress;
        this.myLocation = new Location(longitude, latitude);
        this.myPublicEventIDs = publicIDs;
        this.myPrivateEventIDs = privateIDs;
        populateMyPublicEventList(publicIDs);
        populateMyPrivateEventList(privateIDs);
//        myevents.addAll(myPublicEventList); //TODO need to uncomment
//        myevents.addAll(myPrivateEventList); // TODO need to use eventually for listivew poplation
    }
    public User(String emailAddress,float longitude, float latitude) {
        this.emailAddress = emailAddress;
        this.myLocation = new Location(latitude,longitude);

    }



    // Fetch public events in the ID list from the database
    // This function can also be used to update myPublicEventList given the ID list is correct
    public void populateMyPublicEventList(ArrayList<Integer> publicIDs)
    {
        myPublicEventList = new ArrayList<>();
        for (int i : publicIDs)
        {
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
            // We need to search the public database for this given ID i and return the event
            // Add the found event to the HashSet of public events
        }
    }

    // Fetch private events in the ID list from the database
    // This function can also be used to update myPrivateEventList given the ID list is correct
    public void populateMyPrivateEventList(ArrayList<Integer> privateIDs)
    {
        myPrivateEventList = new ArrayList<>();
        for (int i : privateIDs)
        {
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
            // We need to search the private database for this given ID i and return the event
            // Add the found event to the HashSet of private events
        }
    }

    public Pair<Boolean,Integer> updateLocation(String email, float latitude, float longitude) {
        ThreadUpdateLocation threadUpdateLocation = new ThreadUpdateLocation(email,latitude,longitude);
        new Thread(threadUpdateLocation).start();
        return threadUpdateLocation.getReturnResult();
    }
    // Allows a user to join a given PublicEvent
    public void joinEvent(PublicEvent eventToJoin) {

        // Add user to the events list of attendees
        eventToJoin.attendeeList.add(emailAddress);
        // Add this event to the users list of Events
        myPublicEventIDs.add(eventToJoin.eventID);
        // Add users name/displayname to Map, by default display name will be email
        eventToJoin.emailToDisplay.put(this.emailAddress, this.emailAddress);
        myPublicEventList.add(eventToJoin);
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
    }

    // Allows a user to attempt to join a given PrivateEvent
    // We will return true if the user has the correct uniqueID, false otherwise
    public Boolean joinEvent(PrivateEvent eventToJoin, int eventID) {
        // Check to see if the given uniqueID matches the events uniqueID
        if (eventID == eventToJoin.eventID) {
            // Add user to the events list of attendees
            eventToJoin.attendeeList.add(emailAddress);
            // Add this event to the users list of Events
            myPrivateEventIDs.add(eventToJoin.eventID);
            // Add users name/displayname to Map, by defualt display name will be email
            eventToJoin.emailToDisplay.put(this.emailAddress, this.emailAddress);
            myPrivateEventList.add(eventToJoin);
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
            return true;
        }
        return false;
    }

    // Allows a user to leave a given PublicEvent
    public void leaveEvent(PublicEvent event)
    {
        // Remove event from users things
        myPublicEventIDs.remove(event.eventID);
        populateMyPublicEventList(myPublicEventIDs);
        // Remove user from events things
        if (event.hostList.contains(emailAddress))
            event.hostList.remove(emailAddress);
        if (event.attendeeList.contains(emailAddress))
            event.attendeeList.remove(emailAddress);
        if (event.activeList.contains(emailAddress))
            event.activeList.remove(emailAddress);
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
        // We want to store the updated event into the database
    }

    // Allows a user to leave a given PrivateEvent
    public void leaveEvent(PrivateEvent event)
    {
        // Remove event from users things
        myPrivateEventIDs.remove(event.eventID);
        populateMyPrivateEventList(myPrivateEventIDs);
        // Remove user from events things
        if (event.hostList.contains(emailAddress))
            event.hostList.remove(emailAddress);
        if (event.attendeeList.contains(emailAddress))
            event.attendeeList.remove(emailAddress);
        if (event.activeList.contains(emailAddress))
            event.activeList.remove(emailAddress);
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
        // We want to store the updated event into the database
    }

    // Allows a user to add another user to the host list of a given event
    // If a user is not on the Host list we should not allow them to call this method
    public void addHostToEvent(String userToAddEmail, PublicEvent event)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.hostList.add(userToAddEmail);
            if (!event.attendeeList.contains(userToAddEmail))
            {
                event.attendeeList.add(userToAddEmail);
                ///////////////////////
                //***DATABASE CALL***//
                //***DATABASE CALL***//
                //***DATABASE CALL***//
                ///////////////////////
            }
        }
        // Otherwise we do nothing as the user does not have proper permission to add hosts
    }

    public void addHostToEvent(String userToAddEmail, PrivateEvent event)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.hostList.add(userToAddEmail);
            if (!event.attendeeList.contains(userToAddEmail))
            {
                event.attendeeList.add(userToAddEmail);
                ///////////////////////
                //***DATABASE CALL***//
                //***DATABASE CALL***//
                //***DATABASE CALL***//
                ///////////////////////
            }
        }
        // Otherwise we do nothing as the user does not have proper permission to add hosts
    }

    // Allows user to edit the the timing of the event given that they are a host
    public void editEventTimes(PublicEvent event, Date startTime, Date endTime)
    {
        if (event.hostList.contains(this.emailAddress))
        {
//            event.startTime = startTime;
//            event.endTime = endTime;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    public void editEventTimes(PrivateEvent event, Date startTime, Date endTime)
    {
        if (event.hostList.contains(this.emailAddress))
        {
//            event.startTime = startTime;
//            event.endTime = endTime;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    // Allows user to edit the name of an event given they are on the host list
    public void editEventName(PublicEvent event, String name)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.eventName = name;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }
    public void editEventName(PrivateEvent event, String name)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.eventName = name;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    // Allows user to edit the description of an event given they are on the host list
    public void editEventDesc(PublicEvent event, String desc)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.eventDesc = desc;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }
    public void editEventDesc(PrivateEvent event, String desc)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.eventDesc = desc;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    // Allows user to edit the radius of an event given they are on the host list
    public void editEventRadius(PublicEvent event, int radius)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.radius = radius;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }
    public void editEventRadius(PrivateEvent event, int radius)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.radius = radius;
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    // Allows user to edit the location of an event given they are on the host list
    public void editEventLocation(PublicEvent event, float latitude, float longitude)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.location = new Location(latitude, longitude);
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    public void editEventLocation(PrivateEvent event, float latitude, float longitude)
    {
        if (event.hostList.contains(this.emailAddress))
        {
            event.location = new Location(latitude, longitude);
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
        }
        // Otherwise we do nothing as the user does not have proper permission to edit the event
    }

    // Allows a user to edit their display name for a given event
    public void editEventDisplayName(PublicEvent event, String displayName)
    {
        event.emailToDisplay.put(this.emailAddress, displayName);
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
    }

    public void editEventDisplayName(PrivateEvent event, String displayName)
    {
        event.emailToDisplay.put(this.emailAddress, displayName);
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
    }
}
