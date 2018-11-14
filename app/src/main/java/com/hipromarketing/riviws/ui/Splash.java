package com.hipromarketing.riviws.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.hipromarketing.riviws.MainActivity;
import com.hipromarketing.riviws.utils.FetchData;
import com.litetech.libs.statemanager.StateManger;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

public class Splash extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                                        .setPersistenceEnabled(true)
                                                        .setTimestampsInSnapshotsEnabled(true)
                                                        .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
//        Fabric.with(this, new Crashlytics());
        try {
            db.setFirestoreSettings(settings);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



//        FetchData.getInstance().fetchYoutubeVideos();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
