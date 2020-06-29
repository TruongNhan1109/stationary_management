package com.ttn.stationarymanagement.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class CustomToast {

    // https://github.com/GrenderG/Toasty

    public static void showToast(Context context, String content, int time) {
        Toast.makeText(context, content, time).show();
    }

    public static void showToastSuccesstion(Context context, String content, int time) {
        Toasty.success(context, content, time, true).show();
    }

    public static void showToastInfor(Context context, String content, int time) {
        Toasty.info(context, content, time, true).show();
    }

    public static void showToastWarning(Context context, String content, int time) {
        Toasty.warning(context, content, time, true).show();
    }

    public static void showToastError(Context context, String content, int time) {
        Toasty.error(context, content, time, true).show();
    }

    public static void showToastWithIcon(Context context, String content, int time, Drawable icon) {
        Toasty.normal(context, content, icon).show();
    }
}
