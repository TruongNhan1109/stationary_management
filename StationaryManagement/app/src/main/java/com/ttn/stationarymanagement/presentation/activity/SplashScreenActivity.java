package com.ttn.stationarymanagement.presentation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ttn.stationarymanagement.data.config.Constants;
import com.ttn.stationarymanagement.data.local.SharedPreferenceManage;
import com.ttn.stationarymanagement.utils.LanguageUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String langCode = SharedPreferenceManage.getInstance().getStringDefault(Constants.LANG_CODE, "");

        if (!TextUtils.isEmpty(langCode)) {
            LanguageUtils.changeLanguageNotSave(langCode, this);
        }

        Intent intent = LoadingActivity.getCallingIntent(this);
        startActivity(intent);
        finish();
    }


}