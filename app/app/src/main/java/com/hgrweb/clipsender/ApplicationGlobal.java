package com.hgrweb.clipsender;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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

    private boolean is_init = false;

    private SharedActivity sender;

    public SharedActivity getSender() {
        return sender;
    }

    public void setSender(SharedActivity sender) {
        this.sender = sender;
    }

    private Context context;

    private String broadAddress = "255.255.255.255";

    public String getBroadAddress() {
        return broadAddress;
    }

    public void setBroadAddress(String address) {
        broadAddress = address;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(Context context) {
        if (!is_init) {
            context = context;
            // 初始化， 程序首次安装运行， 程序数据初始化

            JSON_FILE = context.getNoBackupFilesDir().getPath() + "/data.json";
            ReadData();
            is_init = true;
        }
    }

    private String JSON_FILE = "";


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String ReadFileContent(String path) throws IOException {
        String result = "";
         for (String line : Files.readAllLines(Paths.get(path))){
             result += line;
         }

        return result;
    }

    public void ReadData() {
        try {
            if (Files.exists(Paths.get(JSON_FILE))) {


                String str = ReadFileContent(JSON_FILE);
                System.out.println(str);
                JSONObject json = JSONObject.parseObject(str);



                broadAddress = json.getString("IP");
                pwd = json.getString("PASS");
                port = json.getInteger("PORT");

                Log.d("DATA", "数据初始化");
            } else {
                Files.createFile(Paths.get(JSON_FILE));
                SaveData();
                Log.d("DATA", "创建数据文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveData() {
        JSONObject json = new JSONObject();
        json.put("IP", broadAddress);
        json.put("PASS", pwd);
        json.put("PORT", port);

        try {
            Files.delete(Paths.get(JSON_FILE));
            Files.createFile(Paths.get(JSON_FILE));

            // 这里写入的数据不知道为什么会带有尾巴，导致读出的时候不是一个正常的json字符串， 因此添加了上面的文件重建操作
            Files.write(Paths.get(JSON_FILE), json.toJSONString().getBytes(Charset.forName("UTF-8")), StandardOpenOption.WRITE);
            Log.d("DATA", "数据存盘" + json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPwd() {
        return pwd;
    }

    public int getPort() {
        return port;
    }

    public void setPwd(String pwd) {
        // 写入配置文件
        this.pwd = pwd;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
