package com.ducku.myvideocallapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.CommonNotificationBuilder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String CHANNEL_ID = "com.ducku.myvideocallapp.notification";
    private final String CHANNEL_NAME = "Stringee Video Call Notification";
    private final String CHANNEL_DESC = "Channel for Notification";


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        MainActivity.client.registerPushToken(token, new StatusListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyFirebaseMessagingService.this, "Register success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(StringeeError stringeeError) {
                super.onError(stringeeError);
                Toast.makeText(MyFirebaseMessagingService.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size() > 0) {
            Log.d("Stringee Notification", "Message Data Payload: " + message.getData());
            String pushFromStringee = message.getData().get("stringeePushNotification");
            if (pushFromStringee != null) {
                // Receive push notification from Stringee Server
                String data = message.getData().get("data");
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String callStatus = jsonObject.optString("callStatus");
                        if (callStatus.equals("started")) {
                            // Show your incoming call notification or handle something
                            showNotification();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showNotification() {
        NotificationManager manager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        } else {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Incoming Call")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(1, notification);
    }
}

