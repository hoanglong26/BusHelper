package com.example.hoanglong.bushelper.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.hoanglong.bushelper.MainActivity;
import com.example.hoanglong.bushelper.R;


/**
 * Created by hoanglong on 13-Feb-17.
 */

public class SendNotificationTask {
    public static void sendNotification(Context context, String content) {


            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setContentTitle("Bus Helper")
                            .setContentText(content)
                            .setSmallIcon(R.drawable.ic_bus_noti)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                    R.drawable.icon_bus))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(content))
                            .setAutoCancel(true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            Intent intent = new Intent(context, MainActivity.class);

            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            builder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());

    }




}
