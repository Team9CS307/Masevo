package com.example.brianduffy.masevo;
import java.io.Serializable;
import java.util.HashMap;

public class Users implements Serializable {
    //0 is User
    //1 is host
    //2 is owner

    public HashMap<String, PermissionActivePair> userPerm = new HashMap<String, PermissionActivePair>();

    public Users () {

    }

    public Users (HashMap<String, PermissionActivePair> userPerm) {
        this.userPerm = userPerm;
    }
}
