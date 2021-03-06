package com.hgrweb.clipsender;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;

import com.hgrweb.clipsender.net.Helper;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    static String Head = "CLIPSEND";
    ApplicationGlobal global;
    boolean spn_lock = false;
    LinkedList<Integer> select_index = new LinkedList<>();

    @Override
    protected void onStop() {
        super.onStop();
        global.SaveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global = (ApplicationGlobal) getApplication();
        global.ReadData();

        final TextView port_tv = findViewById(R.id.port);
        final EditText port_edit = findViewById(R.id.port_edit);

        port_tv.setText(global.getPort() + "");
        port_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port_edit.setVisibility(View.VISIBLE);
                showSoftInput(MainActivity.this, port_edit);
                focusView =port_edit;
                port_tv.setVisibility(View.GONE);
            }
        });
        port_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    try {
                        String port_str;
                        int port = 0;
                        try {
                            port_str = String.valueOf(port_edit.getText());
                            // 处理端口输入框为空的情况
                            if (port_str.equals("")){
                                port_edit.setVisibility(View.GONE);
                                port_tv.setVisibility(View.VISIBLE);
                                Toast.makeText( MainActivity.this,
                                        "端口号不能为空",
                                        LENGTH_SHORT).show();
                                return;
                            }
                            port = Integer.valueOf(port_str);
                        } catch (Exception e) {
                            port_edit.requestFocus();
                            throw new Exception("请输入数字");
                        }

                        if (port < 1024 || port > 65535) {
                            port_edit.requestFocus();
                            throw new Exception("无效的端口号");
                        }

                        global.setPort(port);
                        port_tv.setText(port_str);

                        port_edit.setVisibility(View.GONE);
                        port_tv.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText( MainActivity.this, 
                                        e.getMessage(), 
                                        LENGTH_SHORT).show();
                    }
                }
            }
        });

        final TextView pass_tv = findViewById(R.id.pass);
        pass_tv.setText(global.getPwd());
        final EditText pass_edit = findViewById(R.id.pass_edit);

        pass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_edit.setVisibility(View.VISIBLE);
                showSoftInput(MainActivity.this, pass_edit);
                focusView = pass_edit;
                pass_tv.setVisibility(View.GONE);
            }
        });

        pass_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pass_str = String.valueOf(pass_edit.getText());
                    if (!pass_str.equals("")) {
                        global.setPwd(pass_str);
                        pass_tv.setText(pass_str);
                    } else {
                        Toast.makeText( MainActivity.this, 
                                        "输入空密码", 
                                        Toast.LENGTH_LONG).show();
                    }
                    pass_edit.setVisibility(View.GONE);
                    pass_tv.setVisibility(View.VISIBLE);
                }
            }
        });

        final Spinner spn = findViewById(R.id.ip_spinner);
        final TextView ip_tv = findViewById(R.id.ip_showed);
        final EditText ip_edit = findViewById(R.id.ip_edit);

        ip_tv.setText(global.getBroadAddress() + "");
        ip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spn.setVisibility(View.VISIBLE);
                ip_tv.setVisibility(View.GONE);
            }
        });

        final ArrayList<String> ips = new ArrayList<>();
        ips.add("255.255.255.255");

        for (String ip : Helper.getIPs()) {
            ips.add(ip);
        }
        ips.add("手动输入ip");

        ArrayAdapter<String> adapter = 
            new ArrayAdapter<String>(   MainActivity.this, 
                                        android.R.layout.simple_list_item_1, 
                                        ips);
        spn.setAdapter(adapter);

        select_index.add(0);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, 
                                        View view, 
                                        int position, long id) {
                if (!spn_lock) {
                    if (position == ips.size() - 1) {
                        // 显示手动输入ip
                        ip_edit.setText("");
                        ip_edit.setVisibility(View.VISIBLE);
                        focusView = ip_edit;
                        showSoftInput(MainActivity.this, ip_edit);
                        spn.setVisibility(View.GONE);
                    } else {
                        global.setBroadAddress(ips.get(position));
                        ip_tv.setText(global.getBroadAddress());
                        ip_tv.setVisibility(View.VISIBLE);

                        spn.setVisibility(View.GONE);
                        select_index.pop();
                        select_index.add(position);
                    }
                } else {
                    spn_lock = false;
                }
            }
            // 永远不会触发， 因为代码设置默认选中第一个
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ip_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String ip_str = String.valueOf(ip_edit.getText());
                    if (ip_str.equals("")) {
                        // 用户未输入任何内容
                        ip_edit.setVisibility(View.GONE);
                        spn.setVisibility(View.VISIBLE);
                        // 重新设置spinner选中到上一个状态
                        spn.setSelection(select_index.get(0));
                        spn_lock = true;
                        return;
                    } else {
                        if (Helper.IPv4Vilid(ip_str)) { // 数据校验通过
                            global.setBroadAddress(ip_str);
                            ip_tv.setText(global.getBroadAddress());
                            ip_tv.setVisibility(View.VISIBLE);
                            ip_edit.setVisibility(View.GONE);
                        } else {
                            Toast.makeText( MainActivity.this, 
                                            "非法IPv4地址！", 
                                            Toast.LENGTH_LONG).show();
                            ip_edit.requestFocus();
                        }
                    }
                }
            }
        });
    }

    View focusView = null;

    // 有edittext获得焦点时，back的行为修改为取消其焦点
    // 没有焦点时则使用默认行为
    @Override
    public void onBackPressed() {
        if (focusView != null){
            focusView.clearFocus();
            focusView = null;
        }else{
            super.onBackPressed();
        }

    }

    public static void showSoftInput(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        opeSystemKeyBoard(editText);
    }

    public static void opeSystemKeyBoard(View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
