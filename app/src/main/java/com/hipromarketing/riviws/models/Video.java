package com.hipromarketing.riviws.models;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class Video extends RealmObject {
    @PrimaryKey
    private String videoId;
    private String videoTitle;
    private String rating;
    private String commentCount;
    private String thumbnail;
    @Ignore
    private List<Reply> replies = new ArrayList<>();
    @Ignore
    private List<User> likes = new ArrayList<>();


    public Video() {
    }


    public Video(VideoObject.Item item){
        this.videoTitle = item.getSnippet().getTitle();
        this.thumbnail = item.getSnippet().getThumbnails().getHigh().getUrl();
        this.videoId = item.getSnippet().getResourceId().getVideoId();

    }


    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
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


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
