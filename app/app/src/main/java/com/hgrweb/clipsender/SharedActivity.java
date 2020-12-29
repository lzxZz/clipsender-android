package com.hgrweb.clipsender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.hgrweb.clipsender.net.Helper;
import com.hgrweb.clipsender.security.Security;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SharedActivity extends AppCompatActivity {
    static String Head = "CLIPSEND";
    static String body = "{\"MimeType\":\"text/plain\",\"Content\":\"senderstr\"}";


    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    public static SharedActivity shared;
    ApplicationGlobal global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        global = (ApplicationGlobal) getApplication();
        shared = this;
        global.init(this);
        global.setSender(this);
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onResume() {

        moveTaskToBack(true);

        super.onResume();

        global.ReadData();
//        moveTaskToBack(true);
        Intent intent = getIntent();

        String action = intent.getAction();
        String type = intent.getType();

        final String data[] = new String[1];
        data[0] = intent.getStringExtra(Intent.EXTRA_TEXT);



        System.out.println("Action" + action);
        System.out.println("Type" + type);
        System.out.println(data[0]);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                boolean succes =  false;

                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Security security = new Security();

                                JSONObject json = new JSONObject();
                                json.put("MimeType", "text/plain");
                                json.put("Content", data[0]);

                                byte[] datas =  security.encrypt(json.toString(), global.getPwd());

                                byte[] head =  Head.getBytes(StandardCharsets.UTF_8);


                                UdpSender sender = new UdpSender();

                                byte[] new_data =byteMerger(head,datas);
                                Looper.prepare();
                                sender.sendMsg(new_data, global.getBroadAddress(), global.getPort());
                                Toast.makeText(SharedActivity.this, "分享至" + global.getBroadAddress(), Toast.LENGTH_LONG).show();

                                global.getSender().finish();
                                Looper.loop();

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } catch (Exception e) {

                }finally {

                }

            } else {
                // Handle other intents, such as being started from the home screen
            }
        }
    }
}
