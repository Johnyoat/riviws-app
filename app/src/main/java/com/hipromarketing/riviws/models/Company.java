package com.hipromarketing.riviws.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {
    private String name;
    private String lng = "";
    private String lat = "";
    private String companyID = "";
    private String category;
    private String averageRating = "0";
    private String locationUrl = "";
    private String imageUrl = "";

    public Company(){

    }

    protected Company(Parcel in) {
        name = in.readString();
        lng = in.readString();
        lat = in.readString();
        companyID = in.readString();
        category = in.readString();
        averageRating = in.readString();
        locationUrl = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lng);
        dest.writeString(lat);
        dest.writeString(companyID);
        dest.writeString(category);
        dest.writeString(averageRating);
        dest.writeString(locationUrl);
        dest.writeString(imageUrl);
    }
}
