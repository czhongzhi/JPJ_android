package com.runbo.jpj.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.minaserver.MinaService;
import com.runbo.jpj.minaserver.SessionManager;
import com.runbo.jpj.util.LogUtil;

public class MinaActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    private MinaReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mina);

        initView();
        registerBroadcast();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.textView1);
        findViewById(R.id.textView2).setOnClickListener(this);
        findViewById(R.id.textView3).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,MinaService.class));
        unregisterReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView2:
                startService(new Intent(this, MinaService.class));
                LogUtil.e("onClick -- MinaService");
                break;
            case R.id.textView3:
                SessionManager.getInstance().writeToServer("NI NICE");
                LogUtil.e("onClick -- writeToServer");
                break;
        }
    }

    private void registerBroadcast() {
        if(receiver != null) return;
        receiver = new MinaReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.runbo.jpj.ssss");
        registerReceiver(receiver,filter);
    }

    private void unregisterReceiver(){
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private class MinaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("message");
            tv.setText(str);
        }
    }
}
