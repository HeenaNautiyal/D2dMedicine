package com.bizhawkz.d2dRoni;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Heena on 3/2/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Context mContext;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification().getBody() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            String mystring = remoteMessage.getNotification().getBody();
            String arr[] = mystring.split(" ", 2);

            String firstWord = arr[0];   //the
            String theRest = arr[1];
            Log.e(TAG,"Notification first word: "+firstWord);
            if (firstWord.equals("Your")){
                sendNotification3(remoteMessage.getNotification().getBody());
             }
            else if(firstWord.equals("This")){
                sendNotification3(remoteMessage.getNotification().getBody());
            }
            else if(firstWord.equals("Please")){

                sendNotification(remoteMessage.getNotification().getBody());
            }

        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendNotification3(String body) {
        Intent intent = new Intent(this, DataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("D2d Medicine")
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(body);

        notificationManager.notify(0, noBuilder.build());
    }

    private void sendNotification2(String body) {
        Intent intent = new Intent(this, OrderRecieve.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("D2d Medicine")
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(body);

        notificationManager.notify(0, noBuilder.build());
    }

    private void sendNotification(String messageId) {
        Intent intent = new Intent(this, ChemistRating.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("D2d Medicine")
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Click here to Rate Us."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText("Click here to Rate Us.");

        notificationManager.notify(0, noBuilder.build());
    }
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            }
            else
            {
                if (message.equals("New order received")) {
                    Intent resultIntent = new Intent(getApplicationContext(), OrderRecieve.class);
                    resultIntent.putExtra("message", message);
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                }
                else if (message.equals("Please Click Here to Rate Us.")){
                    Intent resultIntent = new Intent(getApplicationContext(), ChemistRating.class);
                    resultIntent.putExtra("message", message);
                    Log.e("message rating",message);
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}