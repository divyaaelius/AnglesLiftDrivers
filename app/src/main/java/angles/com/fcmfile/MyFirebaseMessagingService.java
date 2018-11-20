package angles.com.fcmfile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import angles.com.MainActivity;
import angles.com.R;
import angles.com.utils.ConstMethod;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgServiceDriver";
    Bitmap bitmap;
    static int i = 1;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //PreferHelper pHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        ConstMethod.LodDebug(TAG, "From: " + remoteMessage.getFrom());
//        ConstMethod.LodDebug(TAG, "Message noti : " + remoteMessage.getNotification().getBody());
//{smallIcon=small_icon, sound=1, title=this is the title, vibrate=1, recorde_id=102, largeIcon=large_icon, message=this is the demo, tickerText=Ticker text here...Ticker text here...Ticker text here}
        ConstMethod.LodDebug(TAG, "Message data  " + remoteMessage.getData());
        ConstMethod.LodDebug(TAG, "Message data client  " + remoteMessage.getData());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //  Log.d(TAG, "Message data data: " + remoteMessage.getData().get("message"));
            try {
                JSONObject jobPush = new JSONObject(remoteMessage.getData());
                String message = jobPush.getString("message");
                sendNotification(message);

                String client =jobPush.getString("client");
                ConstMethod.LodDebug(TAG, "Message client  " + client);

                JSONArray array=new JSONArray(client);
                for (int j = 0; j <array.length() ; j++) {
                    JSONObject object=array.getJSONObject(j);
                    String date= object.getString("date");
                    sendNotification(date);
                    ConstMethod.LodDebug(TAG, "Message date  " + date);
                }


                ConstMethod.LodDebug(TAG, "Message data client  " );

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            ConstMethod.LodDebug(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        ConstMethod.LodDebug(TAG, "Message data sendNotification msg: " + messageBody);
        //  pHelper=new PreferHelper(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


        //Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beep_cut);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
