package com.cxel.launcher.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private int page;
    private int index;
    private Drawable appIcon;
    private String appName;
    private String packageName;
    private String activityName;

    public AppInfo() {
        this.appIcon = null;
        this.appName = null;
        this.packageName = null;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setActivityName(String name) {
        this.activityName = name;
    }

    public String getActivityName() {
        return activityName;
    }
}
