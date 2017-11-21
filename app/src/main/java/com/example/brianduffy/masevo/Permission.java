package com.example.brianduffy.masevo;

/**
 * Created by Josh on 11/18/2017.
 */

public class Permission {
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

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
