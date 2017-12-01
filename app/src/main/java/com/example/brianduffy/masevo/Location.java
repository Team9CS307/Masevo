package com.example.brianduffy.masevo;

import java.io.Serializable;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class Location implements Serializable{
    public float latitude;
    public float longitude;

    public Location(float latitude, float longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
