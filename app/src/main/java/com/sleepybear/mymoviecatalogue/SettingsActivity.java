package com.sleepybear.mymoviecatalogue;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.sleepybear.mymoviecatalogue.notification.DailyNotifications;
import com.sleepybear.mymoviecatalogue.notification.UpcomingSchedulerTask;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            findPreference("preference_about").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceClick(@NonNull Preference preference) {
            if (preference.getKey().equals("preference_about")) {
                String version = "MASTER";
                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    version = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String appname = getString(R.string.app_name);
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.about_title))
                        .setMessage(getString(R.string.about_content, appname, version, "rosdyana.kusuma@gmail.com", "https://github.com/rosdyana/My-Movie-Catalogue"))
                        .setIcon(getResources().getDrawable(R.drawable.ic_bear))
                        .setPositiveButton(getString(R.string.ok_text), (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            }
            return false;
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private UpcomingSchedulerTask upcomingSchedulerTask;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            findPreference("notifications_release_today_reminder").setOnPreferenceChangeListener(this);
            findPreference("notifications_daily_reminder").setOnPreferenceChangeListener(this);


            upcomingSchedulerTask = new UpcomingSchedulerTask(getActivity());
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object o) {
            boolean isTurnOn = (boolean) o;
            if (preference.getKey().equals("notifications_daily_reminder")) {
                if (isTurnOn) {
                    DailyNotifications.setDailyReminder(getActivity(), getString(R.string.daily_reminder_notification_text), "07:00");
                    Toast.makeText(getActivity(), getString(R.string.daily_reminder_enable), Toast.LENGTH_SHORT).show();
                } else {
                    DailyNotifications.cancelReminder(getActivity());
                    Toast.makeText(getActivity(), getString(R.string.daily_reminder_disable), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            if (preference.getKey().equals("notifications_release_today_reminder")) {
                if (isTurnOn) {
                    upcomingSchedulerTask.createPeriodicTask();
                    Toast.makeText(getActivity(), getString(R.string.upcoming_movie_reminder_enable), Toast.LENGTH_SHORT).show();
                } else {
                    upcomingSchedulerTask.cancelPeriodicTask();
                    Toast.makeText(getActivity(), getString(R.string.upcoming_movie_reminder_disable), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }

    }
}
