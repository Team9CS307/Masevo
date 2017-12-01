package com.example.brianduffy.masevo;

import java.io.Serializable;

public class PermissionActivePair implements Serializable {
    public enum Permission {
        USER, HOST, OWNER
    };

    private Permission perm;
    private boolean active;
    private boolean isBanned;

    public PermissionActivePair (Permission perm, boolean active, boolean isBanned) {
        this.perm = perm;
        this.active = active;
        this.isBanned = isBanned;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public Permission getPermission() {
        return perm;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public void setPermission(Permission perm) {
        this.perm = perm;
    }
}