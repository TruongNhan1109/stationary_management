package com.ldc.projectmaster.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class AppUtils {

    public static boolean isTabletMode(Context context, boolean isLandScape) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float scaleFactor = displayMetrics.density;
        float widthDp = displayMetrics.widthPixels / scaleFactor;
        float heightDp = displayMetrics.heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);
        if(smallestWidth >= 600 && isLandScape) {
            return  true;
        } else {
            return false;
        }

    }

}
