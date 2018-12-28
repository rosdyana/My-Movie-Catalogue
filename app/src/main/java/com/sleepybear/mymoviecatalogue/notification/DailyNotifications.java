package com.sleepybear.mymoviecatalogue.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.sleepybear.mymoviecatalogue.MainActivity;
import com.sleepybear.mymoviecatalogue.R;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class DailyNotifications extends BroadcastReceiver {
    private static final int DAILY_REMINDER_REQUEST_CODE = 100;
    private static final String EXTRA_CONTENT = "CONTENT";
    private static final CharSequence CHANNEL_NAME = "Movie Catalog channel";

    public static void setDailyReminder(@NonNull Context context, String content, String time) {
        Calendar calendar = Calendar.getInstance();

        String timeArray[] = time.split(":");
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        setCalendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        setCalendar.set(Calendar.SECOND, 0);


        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);

        Intent intent = new Intent(context, DailyNotifications.class);
        intent.putExtra(EXTRA_CONTENT, content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static void cancelReminder(@NonNull Context context) {
        Intent intent = new Intent(context, DailyNotifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Objects.requireNonNull(am).cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private static void showNotification(Context context, Class<?> cls, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "channel_01";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bear)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(alarmSound)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Objects.requireNonNull(notificationManager).notify(DAILY_REMINDER_REQUEST_CODE, builder.build());
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        String message = intent.getStringExtra(EXTRA_CONTENT);
        String title = context.getResources().getString(R.string.app_name);
        showNotification(context, MainActivity.class, title, message);
    }


}
