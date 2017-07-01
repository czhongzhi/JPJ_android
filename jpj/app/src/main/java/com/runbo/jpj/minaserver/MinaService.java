package com.runbo.jpj.minaserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.LogUtil;

/**
 * Created by czz on 2017/4/5.
 */
public class MinaService extends Service {
    private ConnectionManager mManager;
    private ConnectionThread thread;

    @Override
    public void onCreate() {
        ConnectionConfig config = new ConnectionConfig.Builder(getApplicationContext())
                .setIp(Constants.IP)
                .setPort(Constants.MINA_SOCKER_PORT)
                .setReadBufferSize(8 * 1024)
                .setConnectionTimeout(10000).builder();
        mManager = new ConnectionManager(config);
        thread = new ConnectionThread();
        thread.start();
        super.onCreate();
        LogUtil.e("MinaService 启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disConnect();
        thread = null;
    }

    public void disConnect() {
        mManager.disContect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ConnectionThread extends Thread {
        private boolean isConnection;

        @Override
        public void run() {
            //super.onLooperPrepared();
            while (true) {
                isConnection = mManager.connect();
                if (isConnection) {
                    LogUtil.e("连接成功");
                    break;
                }
                try {
                    LogUtil.e("尝试重新连接");
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
