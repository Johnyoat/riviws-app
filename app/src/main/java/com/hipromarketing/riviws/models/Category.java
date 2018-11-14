package com.hipromarketing.riviws.models;

public class Category {
    private String category;
    private int imgUrl;

    public Category() {
    }

    public Category(String category, int imgUrl) {
        this.category = category;
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
