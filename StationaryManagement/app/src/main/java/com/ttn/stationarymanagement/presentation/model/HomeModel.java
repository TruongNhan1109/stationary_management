package com.ttn.stationarymanagement.presentation.model;

public class HomeModel {

    private  int idSetting;
    private String settingName;
    private  int icon;

    public HomeModel(int id, String name, int icon) {
        this.idSetting = id;
        this.settingName = name;
        this.icon = icon;

    }

    public int getIdSetting() {
        return idSetting;
    }

    public void setIdSetting(int idSetting) {
        this.idSetting = idSetting;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
