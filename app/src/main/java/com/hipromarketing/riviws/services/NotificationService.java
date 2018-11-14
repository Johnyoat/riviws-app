package com.hipromarketing.riviws.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.models.NotificationObject;
import com.hipromarketing.riviws.models.Statistics;
import com.hipromarketing.riviws.utils.Notifications;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;

public class NotificationService extends Service {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Notifications notifications;
    Realm realm;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        notifications = Notifications.getInstance(getApplicationContext());

        db.collection("users").document(user.getUid()).collection("notifications").whereEqualTo("read",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<NotificationObject> objects = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        NotificationObject obj = snapshot.toObject(NotificationObject.class);
                        assert obj != null;
                        if (obj.isNewpost()) {
                            objects.add(obj);
                        }
                    }

                    saveStatistics(new Statistics(1,String.valueOf(queryDocumentSnapshots.size()),""));


                    notifications.createNotifications(objects);
                }
            }
        });


        db.collection("riviws")
                .whereEqualTo("user.uid", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            saveStatistics(new Statistics(2,"",String.valueOf(queryDocumentSnapshots.size())));
                        }
                    }
                });

//        return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;

    }



    private void saveStatistics(final Statistics statistics){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                    realm.insertOrUpdate(statistics);
            }
        });
    }
}
