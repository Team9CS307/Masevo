package com.example.brianduffy.masevo;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public class User {
    String emailAddress;
    HashMap<Integer, String> myEventList;
    Location myLocation;

    public User(String emailAddress, float longitude, float latitude) {
        this.emailAddress = emailAddress;
        this.myLocation = new Location(longitude, latitude);
        this.myEventList = new HashMap<>();
    }

    // Allows a user to join a given PublicEvent
    public void joinEvent(PublicEvent eventToJoin) {

        // Add user to the events list of attendees
        eventToJoin.attendeeList.add(emailAddress);
        // Add this event to the users list of Events
        myEventList.put(eventToJoin.eventID, eventToJoin.eventName);
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
            myEventList.put(eventToJoin.eventID, eventToJoin.eventName);
            ///////////////////////
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            //***DATABASE CALL***//
            ///////////////////////
            return true;
        }
        return false;
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
            event.startTime = startTime;
            event.endTime = endTime;
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
            event.startTime = startTime;
            event.endTime = endTime;
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
    public void editEventRadius(PublicEvent event, double radius)
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
    public void editEventRadius(PrivateEvent event, double radius)
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
}
