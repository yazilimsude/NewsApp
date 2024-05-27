package com.example.newsnow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "news_channel";
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    int notificationId = 1;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("RANA", "Girdi");

        db = FirebaseDatabase.getInstance("https://buyukveri-7d8ff-default-rtdb.europe-west1.firebasedatabase.app/");
        auth=FirebaseAuth.getInstance();
        Log.d("RANA-id",auth.getCurrentUser().getUid());
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
        Log.d("RANA", "onMessageReceived başlangıç");
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String category = remoteMessage.getData().get("category");
            String key = remoteMessage.getData().get("key");
            Log.d("RANA-Notification", "Notification received");
            Log.d("RANA-Notification", "Title: " + title + ", Body: " + body + ", Category: " + category);

            ref = db.getReference("Users");
            Log.d("RANA-id",auth.getCurrentUser().getUid());
            /*ref.child(key).child("SelectedCategories")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("RANA", "onDataChange girdi");
                            List<String> trueSelectedCategories = new ArrayList<>();
                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                Boolean isSelected = categorySnapshot.getValue(Boolean.class);
                                if (Boolean.TRUE.equals(isSelected)) {
                                    Log.d("RANA", "onDataChange ifine girdi");
                                    String category = categorySnapshot.getKey();
                                    trueSelectedCategories.add(category);
                                }
                            }
                            Log.d("RANA", trueSelectedCategories.toString());
                            // Do something with the true selected categories
                            for (String selectedCategory : trueSelectedCategories) {
                                Log.d("TrueSelectedCategory", selectedCategory);
                                if(category.equals(selectedCategory)){
                                    Log.d("RANA", "Category2: ");
                                    Log.d("RANA", selectedCategory);
                                    showNotification(title,  body);
                                }
                            }
                            Log.d("RANA", "for çıktı");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle possible errors.
                            Log.w("FirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
                        }
                    });*/
            showNotification(title,  body);


        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d("RANA-NewToken", "New token: " + token);
    }

    public void showNotification(String title, String body){
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
