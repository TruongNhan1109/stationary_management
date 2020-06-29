package com.ttn.stationarymanagement.presentation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ttn.stationarymanagement.data.local.DatabaseHelper;
import com.ttn.stationarymanagement.presentation.activity.home.HomeActivity;

import io.reactivex.rxjava3.core.Observable;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = HomeActivity.getCallingIntent(this);
        startActivity(intent);
        finish();
    }


}