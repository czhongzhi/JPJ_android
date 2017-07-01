package com.runbo.jpj.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Stack;

/**
 * Created by czz on 2016/5/20.
 * app activity 管理类
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;
    private PendingIntent restartIntent;

    private AppManager() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 单例堆栈
     * @return
     */
    private Stack<Activity> getStacts() {
        if (activityStack == null) {
            synchronized (AppManager.class) {
                if (activityStack == null) {
                    activityStack = new Stack<>();
                }
            }
        }
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     * @param activity
     */
    public void addActivity(Activity activity) {
        getStacts();
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity(堆栈中最后一个进入的)
     * @return
     */
    public Activity currentActivity(){
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前的Activity
     */
    public void finishActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     * @param cls
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack){
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity(){
        for (Activity activity : activityStack){
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exitApp(){
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
