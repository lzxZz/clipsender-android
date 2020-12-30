package com.hgrweb.clipsender;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationGlobal extends Application {

    private String pwd = "password";
    private int port = 7814;
    private String broadAddress = "255.255.255.255";

    private SharedActivity sender;

    public ApplicationGlobal(){
//        ReadData();
    }

    public void ReadData() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String ip_, pass_;
        int port_;

        port_ = sharedPref.getInt(getString(R.string.setting_port), 7814);
        ip_ = sharedPref.getString(getString(R.string.setting_ip), "255.255.255.255");
        pass_ = sharedPref.getString(getString(R.string.setting_pass), "password");

        setPort(port_);
        setBroadAddress(ip_);
        setPwd(pass_);


    }

    public void SaveData() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.setting_port), getPort());
        editor.putString(getString(R.string.setting_pass), getPwd());
        editor.putString(getString(R.string.setting_ip), getBroadAddress());
        editor.commit();
    }

    public String getPwd() {
        return pwd;
    }

    public int getPort() {
        return port;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SharedActivity getSender() {
        return sender;
    }

    public void setSender(SharedActivity sender) {
        this.sender = sender;
    }

    public String getBroadAddress() {
        return broadAddress;
    }

    public void setBroadAddress(String address) {
        broadAddress = address;
    }

}
