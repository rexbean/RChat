package com.rex.common.common.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.rex.common.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public abstract class Application extends android.app.Application {

    private static Application instance;
    private List<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }

    /**
     * finish all activities
     */
    public void finishAll(){
        for (Activity activity : activities) {
            activity.finish();

            //todo: start an activity
        }
    }

    public static Application getInstance(){
        return instance;
    }

    public void show(final String text){
        Handler myHandler = new Handler(getMainLooper());
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Application.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
