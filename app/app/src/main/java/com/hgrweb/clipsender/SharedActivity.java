package com.hgrweb.clipsender;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

import com.hgrweb.clipsender.security.Security;

import static com.hgrweb.clipsender.security.Security.byteMerger;

public class SharedActivity extends AppCompatActivity {

    static String Head = "CLIPSEND";

    public static SharedActivity shared;
    ApplicationGlobal global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        global = (ApplicationGlobal) getApplication();
        global.ReadData();
        shared = this;
        global.setSender(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        global.ReadData();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        final String data[] = new String[1];
        data[0] = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject();
                                json.put("MimeType", "text/plain");
                                json.put("Content", data[0]);

                                Security security = new Security();
                                byte[] datum = security.encrypt(
                                                json.toString(), 
                                                global.getPwd());
                                byte[] head = Head.getBytes(
                                                StandardCharsets.UTF_8);
                                byte[] new_data = byteMerger(head, datum);

                                UdpSender sender = new UdpSender();
                                Looper.prepare();
                                sender.sendMsg( new_data, 
                                                global.getBroadAddress(), 
                                                global.getPort());
                                Toast.makeText( SharedActivity.this, 
                                            "分享至" + global.getBroadAddress(), 
                                                Toast.LENGTH_LONG).show();
                                global.getSender().finish();
                                Looper.loop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                } finally {
                }
            } else {
            }
        }
    }
}
