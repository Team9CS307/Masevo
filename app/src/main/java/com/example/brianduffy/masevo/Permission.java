package com.example.brianduffy.masevo;

import java.io.Serializable;

/**
 * Created by Josh on 11/18/2017.
 */

public class Permission implements Serializable {
    private int permissionLevel;
    public Permission (int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public boolean hasCreatorPermissions() {
        return (permissionLevel == 2) ? true : false;
    }
    public boolean hasHostPermissions() {
        return (permissionLevel == 1) ? true : false;
    }
    public boolean hasUserPermissions() {
        return (permissionLevel == 0) ? true : false;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }
    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
