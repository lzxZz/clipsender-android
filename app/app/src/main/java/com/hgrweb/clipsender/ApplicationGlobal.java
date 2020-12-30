package com.hgrweb.clipsender;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ApplicationGlobal extends Application {

    private String pwd = "password";
    private int port = 7814;
    private String broadAddress = "255.255.255.255";
    
    private boolean is_init = false;

    private SharedActivity sender;

    public SharedActivity getSender() {
        return sender;
    }

    public void setSender(SharedActivity sender) {
        this.sender = sender;
    }

    private Context context;


    public String getBroadAddress() {
        return broadAddress;
    }

    public void setBroadAddress(String address) {
        broadAddress = address;
    }


    public void init(Context context_) {
        if (!is_init) {
            context = context_;
            // 初始化， 程序首次安装运行， 程序数据初始化

            ReadData();
            is_init = true;
        }
    }

    private String JSON_FILE = "";


    private String ReadFileContent(String path) throws IOException {
        String result = "";
         for (String line : Files.readAllLines(Paths.get(path))){
             result += line;
         }

        return result;
    }

   
    void  ReadDate(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String ip_,pass_;
        int port_;

        port_ = sharedPref.getInt(getString(R.string.setting_port), 7814);
        ip_ = sharedPref.getString(getString(R.string.setting_ip), "255.255.255.255");
        pass_ = sharedPref.getString(getString(R.string.setting_pass), "password");

        setPort(port_);
        setBroadAddress(ip_);
        setPwd(pass_);


    }

    void SaveDate(){

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


}
