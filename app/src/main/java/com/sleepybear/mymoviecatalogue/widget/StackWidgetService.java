package com.sleepybear.mymoviecatalogue.widget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {
    @NonNull
    @Override
    public RemoteViewsFactory onGetViewFactory(@NonNull Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}