package com.runbo.jpj.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.util.SparseBooleanArray;
import android.util.TypedValue;

import com.baidu.mapapi.SDKInitializer;
import com.chat.dbhelper.ChatpocService;
import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.runbo.jpj.R;
import com.runbo.jpj.api.Api;
import com.runbo.jpj.minaserver.ConnectionManager;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.ui.MainActivity;
import com.runbo.jpj.util.FileUtil;
import com.runbo.jpj.util.NetworkUtils;
import com.runbo.jpj.util.PollTimer;

import org.apache.mina.core.session.IoSession;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by czz on 2017/3/24.
 */
public class MyApplication extends Application {

    private static MyApplication app = null;

    public static int MODE_1 = Context.MODE_MULTI_PROCESS+Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;


    private Api networkApi;//网络请求实例
    public static Context mContext;

    public ChatpocService DbService;
    public static JpjGroup currJpjGroup;//当前组的实例
    public static JpjDevice currJpjDevice;//当前设备的实例

    //用来装载某个item是否被选中
    public static SparseBooleanArray selected_g;
    public static SparseBooleanArray selected_c;
    public static int select_child_old = -1;
    public static int select_group_old = -1;
    public static int parentPos = -1;

    public static Map<String,PollTimer> pollTimerMap = new HashMap<>();//保存轮询定时器

    @Override
    public void onCreate() {
        super.onCreate();
        app = MyApplication.this;

        selected_g = new SparseBooleanArray();
        selected_c = new SparseBooleanArray();
        DbService = new ChatpocService(getApplicationContext());

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        //初始化ImageLoader
        initImageLoader(getApplicationContext());


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static MyApplication getInstance(){
        return app;
    }


    public Api getApi(){
        if(networkApi == null){
            networkApi = new Api();
        }
        return networkApi;
    }

    /**
     * 设置当前组id
     * @param groupID
     */
    public void setCurrGroupID(int groupID){
        LoginPrefereces.setCurrGroupID(getApplicationContext(),groupID);
    }

    /**
     * 获取当前组id
     * @return
     */
    public int getCurrGroupID(){
        return LoginPrefereces.getCurrGroupID(getApplicationContext());
    }

    /**
     * 设置当前设备id
     * @param deviceID
     */
    public void setCurrDeviceID(int deviceID){
        LoginPrefereces.settCurrDeviceID(getApplicationContext(),deviceID);
    }

    /**
     * 获取当前设备id
     * @return
     */
    public int getCurrDeviceID(){
        return LoginPrefereces.getCurrDeviceID(getApplicationContext());
    }

    /**
     * 获取当前选中的实例
     */
    public void getCurrSelectEntity(){
        int currGroupID = MyApplication.getInstance().getCurrGroupID();
        int currDeviceID = MyApplication.getInstance().getCurrDeviceID();
        currJpjGroup = DbService.queryJpjGroup(currGroupID);
        currJpjDevice = DbService.queryJpjDevice(currDeviceID);
    }

    public static void initSelectedGroup(){
        MyApplication.selected_g.put(select_group_old,false);
    }

    public static void initSelectedChild(){
        MyApplication.selected_c.put(select_child_old,false);
    }

    public static void setSelectedGroupItem(int group_id){
        if(select_group_old != -1){
            MyApplication.selected_g.put(select_group_old,false);
        }
        MyApplication.selected_g.put(group_id,true);
        select_group_old = group_id;
    }

    public static void setSelectedChildItem(int group_id,int child_id){
        parentPos = group_id;
        if(select_child_old != -1){
            MyApplication.selected_c.put(select_child_old,false);
        }
        MyApplication.selected_c.put(child_id,true);
        select_child_old = child_id;
    }

    /**
     * 检查网络是否可用
     * @return
     */
    public boolean checkNetWork(){
        return NetworkUtils.isNetworkAvilable(getApplicationContext());
    }

    /**
     * 保存登录状态
     * @param number
     * @param password
     * @param savePswStatus
     */
    public void saveLocalLogin(String number,String password,boolean savePswStatus){
        Context c = getApplicationContext();
        LoginPrefereces.setNumber(c,number);
        LoginPrefereces.setPasswd(c,password);
        LoginPrefereces.setLoginStatus(c,true);
        LoginPrefereces.setSavePswStatus(c,savePswStatus);
    }

    /**
     * 退出时删除登录状态
     */
    public void delectLocalLogin(){
        Context c = getApplicationContext();
        LoginPrefereces.setLoginStatus(c,false);
    }

    /**
     * 获取软件版本Name
     * @param c
     * @return
     */
    public static String getVersionName(Context c){
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(c.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取软件版本Code
     * @param c
     * @return
     */
    public static int getVersionCode(Context c){
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(c.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 安装apk文件
     */
    public static void installAPK(Context context, String app_name) {
        Uri uri = Uri.fromFile(FileUtil.createFile(context, app_name));
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
    }

    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.jpj_bg)
            .showImageForEmptyUri(R.drawable.jpj_bg)
            .showImageOnFail(R.drawable.jpj_bgerror)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer())
            .build();

    public static void initImageLoader(Context context){
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }
}
