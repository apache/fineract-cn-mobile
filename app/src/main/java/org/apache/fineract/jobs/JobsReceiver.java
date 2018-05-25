package org.apache.fineract.jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.ui.online.DashboardActivity;

public class JobsReceiver extends BroadcastReceiver {

    private PreferencesHelper preferencesHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferencesHelper = new PreferencesHelper(context);
        StartSyncJob.JobStatus jobStatus = (StartSyncJob.JobStatus) intent.
                getSerializableExtra(StartSyncJob.STATUS);
        String msg;
        if (jobStatus == StartSyncJob.JobStatus.STARTED) {
            msg = "Fetching data from server, we will notify you once its done.";
            preferencesHelper.setFetching(true);
        } else {
            msg = "Fetching data complete.";
            preferencesHelper.setFetching(false);
        }
        showNotification(context, msg);

    }

    private void showNotification(Context context, String msg) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("NOTIFICATION_CHANNEL");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.launcher_image)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true);
        Intent intent = new Intent(context, DashboardActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(123, mBuilder.build());
    }

}
