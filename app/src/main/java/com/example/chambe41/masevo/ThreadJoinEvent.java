package com.example.chambe41.masevo;

/**
 * Created by Brian Duffy on 11/30/2017.
 */

public class ThreadJoinEvent implements Runnable {
    int id;
    String email;
    public ThreadJoinEvent(int id, String email) {
        this.id = id;
        this.email = email;

    }
    @Override
    public void run() {

    }
}
