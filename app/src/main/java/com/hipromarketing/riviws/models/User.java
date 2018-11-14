package com.hipromarketing.riviws.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

public class User implements Parcelable {
    private String uid ="";
    private String userName="";
    private String email="";
    private String phoneNumber="";
    private String profilePhotoUrl="";
    private String location="";
    private String occupation="";

    public User(FirebaseUser user) {
        this.userName = user.getDisplayName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        if (user.getPhotoUrl() != null && user.getPhotoUrl().toString() != null){
            this.profilePhotoUrl = user.getPhotoUrl().toString();
        }else {
            this.profilePhotoUrl = "";
        }

        this.uid = user.getUid();

    }


    public User(){

    }

    protected User(Parcel in) {
        uid = in.readString();
        userName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        profilePhotoUrl = in.readString();
        location = in.readString();
        occupation = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(profilePhotoUrl);
        parcel.writeString(location);
        parcel.writeString(occupation);
    }
}
