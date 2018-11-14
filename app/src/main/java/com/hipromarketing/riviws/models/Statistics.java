package com.hipromarketing.riviws.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Statistics extends RealmObject {
    @PrimaryKey
    private long id;
    private String notifications;
    private String  reviews;


    public Statistics(long id,String notifications, String reviews) {
        this.notifications = notifications;
        this.reviews = reviews;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }




    public Statistics() {
    }



    public Statistics(String notifications) {
        this.notifications = notifications;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}
