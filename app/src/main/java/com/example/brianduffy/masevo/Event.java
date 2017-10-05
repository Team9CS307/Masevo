package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public interface Event {
    public Location getLocation();
        // Returns the Location of a given event
    public SimpleDateFormat getStartTime();
        // Returns the start time of a given event in the form of SimpleDateFormat
    public SimpleDateFormat getEndTime();
        // Returns the end time of a given event in the form of SimpleDateFormat
    public void setStartTime(SimpleDateFormat startTime);
        // Receives a given SimpleDateFormat to use as the start time of an event
        // May want to add option of return 1 upon valid start time, 0 upon invalid start time
    public void setEndTime(SimpleDateFormat endTime);
        // Receives a given SimpleDateFormat to use as the end time of an event
        // May want to add option of return 1 upon a valid end time, 0 upon invalid end time
    public void setLocation(Location location);
        // Receives a given Location to use as the Location of an event;
        // May want to add option of return 1 upon success, 0 upon failure/invalid input
}
