package com.hipromarketing.riviws.models;

public class NotificationObject {
    private String id;
    private User user;
    private String postId;
    private String action;
    private boolean read;
    private boolean newpost;
    private String company;
    private String date;

    public NotificationObject() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isNewpost() {
        return newpost;
    }

    public void setNewpost(boolean newpost) {
        this.newpost = newpost;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
