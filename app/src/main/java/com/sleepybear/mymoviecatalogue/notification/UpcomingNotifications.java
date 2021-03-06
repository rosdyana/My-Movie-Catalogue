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

import com.google.gson.Gson;
import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class UpcomingNotifications extends BroadcastReceiver {
    private static final int UPCOMING_MOVIE_REMINDER_REQUEST_CODE = 101;
    private static final String EXTRA_CONTENT = "CONTENT";
    private static final String EXTRA_TITLE = "TITLE";
    private static final CharSequence CHANNEL_NAME = "Movie Catalog channel";
    private final Gson gson = new Gson();

    public static void setUpcomingMovieReminder(@NonNull Context context, String title, String content, String date, String time, Result items) {
        String dateArray[] = date.split("-");
        String timeArray[] = time.split(":");
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        setCalendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        setCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
        setCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        setCalendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        setCalendar.set(Calendar.SECOND, 0);


        Intent intent = new Intent(context, UpcomingNotifications.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(items));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UPCOMING_MOVIE_REMINDER_REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Objects.requireNonNull(am).setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private static void showNotification(Context context, Class<?> cls, String title, String content, Result result) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(result));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UPCOMING_MOVIE_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "channel_02";
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

        Objects.requireNonNull(notificationManager).notify(UPCOMING_MOVIE_REMINDER_REQUEST_CODE, builder.build());
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        String message = intent.getStringExtra(EXTRA_CONTENT);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String jsonitem = intent.getStringExtra(MovieDetail.MOVIE_RESULT);
        Result result = gson.fromJson(jsonitem, Result.class);
        showNotification(context, MovieDetail.class, title, message, result);
    }
}
