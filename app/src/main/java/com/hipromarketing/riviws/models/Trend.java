package com.hipromarketing.riviws.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Trend  implements Parcelable{
    private String id;
    private String comment ="";
    private String imageUrl ="";
    private String date ="";
    private String rating ="0";
    private User user;
    private Company company = new Company();
    private List<Reply> replies = new ArrayList<>();
    private List<User> likes = new ArrayList<>();



    public Trend(){

    }


    protected Trend(Parcel in) {
        id = in.readString();
        comment = in.readString();
        imageUrl = in.readString();
        date = in.readString();
        rating = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        likes = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Trend> CREATOR = new Creator<Trend>() {
        @Override
        public Trend createFromParcel(Parcel in) {
            return new Trend(in);
        }

        @Override
        public Trend[] newArray(int size) {
            return new Trend[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(comment);
        dest.writeString(imageUrl);
        dest.writeString(date);
        dest.writeString(rating);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(likes);
    }
}
