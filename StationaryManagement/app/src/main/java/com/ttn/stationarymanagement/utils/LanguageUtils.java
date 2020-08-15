package com.ttn.stationarymanagement.utils;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.ttn.stationarymanagement.data.config.Constants;
import com.ttn.stationarymanagement.data.local.SharedPreferenceManage;
import com.ttn.stationarymanagement.presentation.baseview.MyApp;

import java.util.Locale;


public class LanguageUtils {

    public static void changeLanguage(String languageCode, Context context) {

        // Lưu ngôn ngữ được thiết lập
        SharedPreferenceManage.getInstance().putString(Constants.LANG_CODE, languageCode);

        Locale locale = new Locale(languageCode);
        locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    public static void changeLanguageNotSave(String languageCode, Context context) {

        Locale locale = new Locale(languageCode);
        locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }


}
