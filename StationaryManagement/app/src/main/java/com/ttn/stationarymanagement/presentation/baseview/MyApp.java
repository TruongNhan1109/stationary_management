package com.ttn.stationarymanagement.presentation.baseview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyApp extends Application {

    private static Context context;
    private static Activity activity;
    private static boolean isBackGround = false;
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        MyApp.context = getApplicationContext();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Foreground.init(this);
    }

    public void showToast(final String message, final int duration) {
        Toast.makeText(MyApp.this, message, duration);
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

    public static Activity getActivity() {
        return activity;
    }




    //---------------------------------------------------------------------------------

    static class Foreground implements Application.ActivityLifecycleCallbacks {

        private static Foreground instance;

        public static void init(Application app) {
            if (instance == null) {
                instance = new Foreground();
                app.registerActivityLifecycleCallbacks(instance);
            }
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            MyApp.activity = activity;
            Log.d("MyApp", "onActivityStarted: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            Log.d("MyApp", "onActivityResumed: " + activity.getLocalClassName());
            MyApp.activity = activity;
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }

}
