package com.example.extarc.androidpushnotification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ReminderNotification extends BroadcastReceiver {

    private final String TAG = "ReminderNotification";

    @Override
    public void onReceive(Context context, Intent intent) {

        String Title = intent.getStringExtra(context.getString(R.string.TitleReminder));
        String content = intent.getStringExtra(context.getString(R.string.ContentReminder));

        Log.d(TAG, "Title & Content" + Title + content);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.alarmclock)
                        .setContentTitle(Title)
                        .setContentText(content).setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notify"));

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
