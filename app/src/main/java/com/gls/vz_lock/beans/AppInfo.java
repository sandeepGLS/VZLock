package com.gls.vz_lock.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by Santosh on 11-Dec-17.
 */

public class AppInfo {
    int appId;
    String appname = "";
    Drawable icon;
    boolean locked = true;
    String appPackage;
    boolean onHome;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public boolean isOnHome() {
        return onHome;
    }

    public void setOnHome(boolean onHome) {
        this.onHome = onHome;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
