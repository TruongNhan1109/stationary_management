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

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void showToast(final String message, final int duration) {
        Toast.makeText(MyApp.this, message, duration);
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}
