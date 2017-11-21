package com.example.brianduffy.masevo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Josh on 11/18/2017.
 */

public class EventUsers {
    private ArrayList<String> emails;
    private ArrayList<Permission> permissions;
    private ArrayList<Boolean> actives;

    public EventUsers(ArrayList<String> emails, ArrayList<Permission> permissions, ArrayList<Boolean> actives) {
        this.emails = emails;
        this.permissions = permissions;
        this.actives = actives;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public ArrayList<Boolean> getActives() {
        return actives;
    }

    public void setActives(ArrayList<Boolean> actives) {
        this.actives = actives;
    }
}
