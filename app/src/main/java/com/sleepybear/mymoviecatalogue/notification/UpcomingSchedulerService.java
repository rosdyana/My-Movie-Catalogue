package com.sleepybear.mymoviecatalogue.notification;

import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingMovieModel;
import com.sleepybear.mymoviecatalogue.utils.Utils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingSchedulerService extends GcmTaskService {
    public static final String TAG_TASK_UPCOMING_MOVIE_UPDATE = "TAG_TASK_UPCOMING_MOVIE_UPDATE";

    @Override
    public int onRunTask(@NonNull TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING_MOVIE_UPDATE)) {
            getUpcomingMovie();
            return GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getUpcomingMovie() {
        APIService service = NetworkInstance.getRetrofitInstance().create(APIService.class);
        String currentLanguage = Utils.getDeviceLang(Locale.getDefault().getDisplayLanguage());
        Call<UpcomingMovieModel> upcomingMovieModelCall = service.getUpcomingMovie(currentLanguage);
        upcomingMovieModelCall.enqueue(new Callback<UpcomingMovieModel>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingMovieModel> call, @NonNull Response<UpcomingMovieModel> response) {
                if (response.isSuccessful()) {
                    List<Result> items = Objects.requireNonNull(response.body()).getResults();
                    for (int i = 0; i < Objects.requireNonNull(items).size(); i++) {
                        if (items.get(i).getReleaseDate().compareTo(Utils.getCurrentDate()) >= 0) {
                            String content = getString(R.string.upcoming_reminder_text, items.get(i).getOriginalTitle());
                            Result results = items.get(i);
                            UpcomingNotifications.setUpcomingMovieReminder(getApplicationContext(), items.get(i).getOriginalTitle(), content, items.get(i).getReleaseDate(), "08:00", results);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingMovieModel> call, @NonNull Throwable t) {

            }
        });
    }


}
