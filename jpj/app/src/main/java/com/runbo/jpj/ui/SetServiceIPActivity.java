package com.runbo.jpj.ui;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.runbo.jpj.R;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.util.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetServiceIPActivity extends BaseActivity implements View.OnClickListener {

    private ImageView bar_back;
    private TextView bar_title,bar_right;
    private EditText set_content;
    private Button set_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_service_ip);

        initView();
    }

    private void initView() {
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_right = (TextView) findViewById(R.id.bar_right);
        bar_title = (TextView) findViewById(R.id.bar_title);
        set_content = (EditText) findViewById(R.id.set_content);
        set_ok = (Button) findViewById(R.id.set_ok);

        bar_right.setOnClickListener(this);
        bar_back.setOnClickListener(this);
        set_ok.setOnClickListener(this);

        bar_title.setText("修改服务器地址");

        String ip = LoginPrefereces.getIp(this);
        if(TextUtils.isEmpty(ip)){
            set_content.setHint("请输入IP地址");
        }else{
            set_content.setHint(ip);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_right:
            case R.id.bar_back:
                finish();
                break;
            case R.id.set_ok:
                saveIP();
                break;
        }
    }

    private void saveIP() {
        String ip = set_content.getText().toString().trim();
        if(TextUtils.isEmpty(ip)){
            ToastUtil.show(this,"内容不能为空！");
            return;
        }
        if (!verifyIP(ip)){
            ToastUtil.show(this,"IP地址不规范！");
            return;
        }

        LoginPrefereces.setIp(this,ip);
        ToastUtil.show(this,"修改成功");
        finish();
    }

    private boolean verifyIP(String ip){
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip); //以验证127.400.600.2为例
        return matcher.matches();
    }


}
