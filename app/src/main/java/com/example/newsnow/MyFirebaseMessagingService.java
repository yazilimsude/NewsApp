package com.example.newsnow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "news_channel";
    int notificationId = 1;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("RANA", "Girdi");
        notificationManager=(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        Log.d("RANA", "Çıktı");

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "News Channel";
            String description = "Channel for news notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                Log.d("RANA", "Null değil");
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle the received notification

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d("RANA-Notification", "Notification received");
            Log.d("RANA-Notification", "Title: " + title + ", Body: " + body);

            // Show the notification
            Log.d("RANA", "if oncesi");
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"example channel",NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle(title)
                    .setContentText(body);
            Log.d("RANA", "notify oncesi");
            notificationManager.notify(1,builder.build());
            Log.d("RANA", "notify sonrasi");
        }
    }

    @Override
    public void onNewToken(String token) {
        // Yeni token alındığında yapılacak işlemleri buraya yazın
        Log.d("RANA-NewToken", "New token: " + token);
    }
}
