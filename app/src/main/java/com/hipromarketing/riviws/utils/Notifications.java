package com.hipromarketing.riviws.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.NotificationObject;
import com.hipromarketing.riviws.ui.BaseHome;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.hipromarketing.riviws.constants.Constants.NOTIFICATION;

public class Notifications {
    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "20005";
    private static final String GROUP_KEY = "202400";


    public static Notifications getInstance(Context context) {
        return new Notifications(context);
    }

    private Notifications(Context context) {
        mContext = context;
    }

    @SuppressLint("StaticFieldLeak")
    private void createNotification(final String title, final String message, String profileImg) {
        final int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        /*Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext, BaseHome.class);
        resultIntent.putExtra(NOTIFICATION, true);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);

//        new AsyncTask<NotificationObject, Void, Bitmap>() {
//
//            @Override
//            protected Bitmap doInBackground(NotificationObject... obj) {
//                try {
//                    URL url = new URL(obj[0].getUser().getProfilePhotoUrl());
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream input = connection.getInputStream();
//                    return BitmapFactory.decodeStream(input);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                super.onPostExecute(bitmap);
//                mBuilder.setContentTitle(title)
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setGroup(GROUP_KEY)
//                        .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
//                        .setStyle(new NotificationCompat.BigTextStyle())
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setLargeIcon(bitmap)
//                        .setContentIntent(resultPendingIntent);
//
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//                }
//
//                NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    int importance = NotificationManager.IMPORTANCE_HIGH;
//                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//                    notificationChannel.enableLights(true);
//                    notificationChannel.setLightColor(Color.RED);
//                    notificationChannel.enableVibration(true);
//                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    assert mNotificationManager != null;
//                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//                    mBuilder.setGroup(GROUP_KEY);
//                    mBuilder.setSmallIcon(R.drawable.ic_notifcation);
//                    mNotificationManager.createNotificationChannel(notificationChannel);
//                }
//                assert mNotificationManager != null;
//                mNotificationManager.notify("Riviws", m /* Request Code */, mBuilder.build());
//            }
//        };


        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(GROUP_KEY)
                .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
                .setGroupSummary(true);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mBuilder.setGroup(GROUP_KEY);
            mBuilder.setGroupSummary(true);
            mBuilder.setSmallIcon(R.drawable.ic_notifcation);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify("Riviws", m /* Request Code */, mBuilder.build());
    }


    public void createNotifications(List<NotificationObject> objects) {
        for (NotificationObject obj : objects) {
            if (obj.getUser() != null){
                createNotification("Riviws", obj.getUser().getUserName() + " " + obj.getAction() + " your review", obj.getUser().getProfilePhotoUrl());
            }
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class MyAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... link) {

            try {
                URL url = new URL(link[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }
}

