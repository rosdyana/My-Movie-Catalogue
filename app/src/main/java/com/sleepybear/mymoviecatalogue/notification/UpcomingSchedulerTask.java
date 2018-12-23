package com.sleepybear.mymoviecatalogue.notification;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class UpcomingSchedulerTask {
    private GcmNetworkManager gcmNetworkManager;

    public UpcomingSchedulerTask(Context context) {
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask() {
        Task periodicTask = new PeriodicTask.Builder()
                .setService(UpcomingSchedulerService.class)
                .setPeriod(3600)
                .setFlex(10)
                .setTag(UpcomingSchedulerService.TAG_TASK_UPCOMING_MOVIE_UPDATE)
                .setPersisted(true)
                .build();
        gcmNetworkManager.schedule(periodicTask);
    }

    public void cancelPeriodicTask() {
        if (gcmNetworkManager != null) {
            gcmNetworkManager.cancelTask(UpcomingSchedulerService.TAG_TASK_UPCOMING_MOVIE_UPDATE, UpcomingSchedulerService.class);
        }
    }
}
