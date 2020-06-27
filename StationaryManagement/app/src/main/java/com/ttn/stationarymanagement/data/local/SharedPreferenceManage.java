package com.ttn.stationarymanagement.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttn.stationarymanagement.data.config.Constants;
import com.ttn.stationarymanagement.presentation.baseview.MyApp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceManage {

    private static SharedPreferenceManage sharedPreferenceManage = null;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPreferenceManage getInstance() {
        if (sharedPreferenceManage == null) {
            sharedPreferenceManage = new SharedPreferenceManage();
        }
        return sharedPreferenceManage;
    }

    private SharedPreferenceManage() {
        context = MyApp.getAppContext();
        sharedPreferences = context.getSharedPreferences(Constants.CONFIG_APP, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringDefault(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putInt(String key, int val) {
        editor.putInt(key, val);
        editor.commit();
    }

    public Integer getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public Integer getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanTrueDefault(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return sharedPreferences.getBoolean(key, defaultVal);
    }

    public void putBoolean(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.commit();
    }

    public void putLong(String key, long val) {
        editor.putLong(key, val);
        editor.commit();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public Integer getInt(String keyShare, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putInt(String keyShare, String key, int val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public String getStringReturnNull(String keyShare, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public String getString(String keyShare, String key) {
        SharedPreferences sharedPreferences = MyApp.getAppContext().getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void putString(String keyShared, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public boolean getBoolean(String keyShare, String key, boolean defaultVal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultVal);
    }

    public void putBoolean(String keyShare, String key, boolean val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public void putFloat(String keyShare, String key, float val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyShare, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    public ArrayList<String> getStringNoticication(String keyShare) {
        Set<String> set = sharedPreferences.getStringSet(keyShare, new HashSet<String>());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(set);
        return arrayList;
    }

    public HashMap<String, Boolean> getHashMap(String keyShare) {
        String stringHashMap = sharedPreferences.getString(keyShare, "");
        HashMap<String, Boolean> hashMap = new HashMap<>();
        if (!stringHashMap.equalsIgnoreCase("")) {
            Type type = new TypeToken<HashMap<String, Boolean>>() {
            }.getType();
            Gson gson = new Gson();
            hashMap = gson.fromJson(stringHashMap, type);
        }
        return hashMap;
    }

    public void addHashMap(String key, HashMap<String, Boolean> value) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(value);
        editor.putString(key, hashMapString);
        editor.commit();
    }

}
