package com.hipromarketing.riviws.models;

import com.hipromarketing.riviws.ui.Following;

public class Follow {
    private String id;
    private String followTag = "";
    private boolean notification = false;
    private boolean fromCompany = false;


    public Follow() {
    }

    public Follow(String followTag, boolean notification,String id,boolean fromCompany) {
        this.id = id;
        this.followTag = followTag;
        this.notification = notification;
        this.fromCompany = fromCompany;
    }

    public String getFollowTag() {
        return followTag;
    }

    public void setFollowTag(String followTag) {
        this.followTag = followTag;
    }


    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(boolean fromCompany) {
        this.fromCompany = fromCompany;
    }
}
