package com.ttn.stationarymanagement.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDataToCommunicate {

    public static String parseDateToString(Date date, String format) {
        if (date == null || TextUtils.isEmpty(format)) {
            return "";
        }
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return  simpleDateFormat.format(date);

        } catch (Exception e) {
            return "";
        }
    }

    public static String getStringFromDate(Date date) {

        if (date == null) {
            return "";
        }
        try {

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date);

        }catch (Exception e) {
            return "";
        }
    }

    public static String changeDateToString(Date date) {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return dateTime;
    }

    public static Date changeStringDateToDate(String format, String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getCurrentDate() {
        String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        return dateTime;
    }

    public static String changeToPrice(double price) {
        String format = "#,### đ";
        DecimalFormat dec;

        try {
            dec = new DecimalFormat(format);
            return dec.format(price);
        } catch (Exception e) {
            return price + "";
        }

    }

    public static String convertStringToString(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        } else {
            return data;
        }

    }


}
