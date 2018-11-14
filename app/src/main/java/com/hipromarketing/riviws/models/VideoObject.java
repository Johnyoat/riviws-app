package com.hipromarketing.riviws.models;

import java.util.List;

public class VideoObject {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public class Item {

        private Snippet snippet;


        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }


    }


    public class ID {
        String videoId;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    public class Snippet {
        private String publishedAt;
        private String channelId;
        private String title;
        private String description;
        private Thumbnails thumbnails;
        private ID resourceId;

        public ID getResourceId() {
            return resourceId;
        }

        public void setResourceId(ID resourceId) {
            this.resourceId = resourceId;
        }


        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }
    }

    public class Thumbnails {
        private High high;

        public High getHigh() {
            return high;
        }

        public void setHigh(High high) {
            this.high = high;
        }
    }

    public class High {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

