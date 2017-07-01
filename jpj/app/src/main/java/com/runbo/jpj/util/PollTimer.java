package com.runbo.jpj.util;

import android.os.Bundle;
import android.os.Message;

import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.ui.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮询定时器
 * Created by czz on 2017/5/6.
 */
public class PollTimer {
    private Timer timer;
    private String device_id;//设备id
    private String channel_No;//通道号，主副摄像头
    private String device_name;//设备名称
    private String lastTime;//数据库该最新图片时间
    private int count = 0;//计数
    private int totalRun = 8;//定时器可循环总次数；

    public PollTimer(String device_name, String device_id,String channel_No,String lastTime){
        this.device_id = device_id;
        this.channel_No = channel_No;
        this.lastTime = lastTime;
        this.device_name = device_name;

        MyApplication.pollTimerMap.put(device_id+"."+channel_No,this);
    }

    public void startTimer(){
        if(timer == null){
            timer = new Timer();

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    //计数判断
                    count++;
                    if(count > totalRun){
                        stopTimer();
                        return;
                    }

                    //使用Handler通信
                    Bundle bundle = new Bundle();
                    bundle.putString("device_id",device_id);
                    bundle.putString("channle_No",channel_No);
                    bundle.putString("lastTime",lastTime);

                    Message msg = new Message();
                    msg.what = MainActivity.START_POLL_GETPIC;
                    msg.setData(bundle);
                    if(MainActivity.mHandler != null){
                        LogUtil.e("MainActivity.mHandler sendMessage");
                        MainActivity.mHandler.sendMessage(msg);
                    }else {
                        LogUtil.e("MainActivity.mHandler is null");
                    }
                }
            };
            timer.schedule(task,8000,8000);
        }
    }

    public void stopTimer(){
        if(timer != null){
            LogUtil.e("PollTimer stopTimer");
            timer.cancel();
            MyApplication.pollTimerMap.remove(device_id+"."+channel_No);
            timer = null;
        }
    }

    public String getDevice_name(){
        return device_name;
    }
}
