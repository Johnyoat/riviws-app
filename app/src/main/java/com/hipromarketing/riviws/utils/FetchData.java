package com.hipromarketing.riviws.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hipromarketing.riviws.models.Video;
import com.hipromarketing.riviws.models.VideoObject;
import com.litetech.libs.restservicelib.RestService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.hipromarketing.riviws.constants.Constants.API_VIDEO_DATA;

public class FetchData implements RestService.CallBack{
    private List<Video> videos;
    public static FetchData getInstance() {
        return new FetchData();
    }

    private FetchData(){

    }

    public void fetchYoutubeVideos(){
        RestService apiCall = new RestService(this);
        apiCall.execute(API_VIDEO_DATA);
    }


    @Override
    public void onResult(String response, String s1) {
        Realm realm = Realm.getDefaultInstance();
        if (response != null) {
            Gson gson = new Gson();
            VideoObject videoObject = gson.fromJson(response, VideoObject.class);
            videos = new ArrayList<>();
            for (VideoObject.Item item : videoObject.getItems()) {
                videos.add(new Video(item));
            }

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insertOrUpdate((videos));
                }
            });

        }
    }

}
