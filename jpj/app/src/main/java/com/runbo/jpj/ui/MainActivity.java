package com.runbo.jpj.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chat.entity.JpjPicRecord;
import com.chat.entity.UpdateInfo;
import com.runbo.jpj.R;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.constants.Constants.BroadCastAction;
import com.runbo.jpj.fragment.BrowseFragment;
import com.runbo.jpj.fragment.MapFragment;
import com.runbo.jpj.fragment.GroupFragment;
import com.runbo.jpj.fragment.SetFragment;
import com.runbo.jpj.fragment.HistoryFragment;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.server.UpdateService;
import com.runbo.jpj.util.AppManager;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.PollTimer;
import com.runbo.jpj.view.MyLoadingDialog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView main_title;

    private GroupFragment fm_group;  //区域群组
    private MapFragment fm_map;   //电子地图
    private BrowseFragment fm_browse;  //图片浏览
    private HistoryFragment fm_his;    //历史查询
    private SetFragment fm_set;    //设置

    private BroadReceiver broadReceiver;

    private UpdateInfo info, info_1;
    private boolean flag_lg = false;
    private String versionname;
    private LayoutInflater mLayoutInflater;
    private View vi;
    private CheckBox checkBox1;

    private static LinearLayout ll_text;

    private MyLoadingDialog dialog;

    public static final int SHOW_UPDATE_VERSION = 100;//版本更新
    public static final int START_POLL_GETPIC = 101;//手动拍照轮询查询

    public static MyHandler mHandler = null;
    public int notifID = 1;//通知id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.mContext = this;
        mHandler = new MyHandler();
        registerReceiver();
        initView();

        versionname = MyApplication.getVersionName(this);
        new UpdateVersionThread().start();

    }

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_UPDATE_VERSION://提示是否更新版本
                    showUpdateDialog();
                    break;
                case START_POLL_GETPIC://手动拍照轮询查询
                    LogUtil.e("MainActivity -- MyHandler -- poll get pic ");
                    Bundle bundle = msg.getData();
                    String device_id = bundle.getString("device_id");
                    String channle_No = bundle.getString("channle_No");
                    String lastTime = bundle.getString("lastTime");
                    pollGetPic(device_id,channle_No,lastTime);
                    break;
            }
        }
    }

    private void initView() {
        main_title = (TextView) findViewById(R.id.id_main_title);
        findViewById(R.id.id_tab_group).setOnClickListener(this);
        findViewById(R.id.id_tab_map).setOnClickListener(this);
        findViewById(R.id.id_tab_browse).setOnClickListener(this);
        findViewById(R.id.id_tab_history).setOnClickListener(this);
        findViewById(R.id.id_tab_set).setOnClickListener(this);

        mLayoutInflater = LayoutInflater.from(mContext);
        vi = getLayoutInflater().inflate(R.layout.context_updata, null);
        checkBox1 = (CheckBox) vi.findViewById(R.id.checkBox1);
        ll_text = (LinearLayout) vi.findViewById(R.id.ll_text);

        fm_group = new GroupFragment();
        fm_map = new MapFragment();
        fm_browse = new BrowseFragment();
        fm_his = new HistoryFragment();
        fm_set = new SetFragment();
        setDefaultFragment(fm_group);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_tab_group:
                initClickIcon();
                changeClickIcon(icon0,R.drawable.bb_btn_home_select);
                switchContent(fm_group);
                break;
            case R.id.id_tab_map:
                initClickIcon();
                changeClickIcon(icon1,R.drawable.bb_btn_label_select);
                switchContent(fm_map);
                break;
            case R.id.id_tab_browse:
                initClickIcon();
                switchContent(fm_browse);
                break;
            case R.id.id_tab_history:
                initClickIcon();
                changeClickIcon(icon3,R.drawable.bb_btn_message_select);
                switchContent(fm_his);
                break;
            case R.id.id_tab_set:
                initClickIcon();
                changeClickIcon(icon4,R.drawable.bb_btn_account_select);
                switchContent(fm_set);
                break;
        }
    }

    private ImageView icon0,icon1,icon3,icon4;
    private void initClickIcon(){
        if (icon0 == null){
            icon0 = (ImageView) findViewById(R.id.icon0);
            icon1 = (ImageView) findViewById(R.id.icon1);
            icon3 = (ImageView) findViewById(R.id.icon3);
            icon4 = (ImageView) findViewById(R.id.icon4);
        }
        icon0.setImageResource(R.drawable.bb_btn_home_unselect);
        icon1.setImageResource(R.drawable.bb_btn_label_unselect);
        icon3.setImageResource(R.drawable.bb_btn_message_unselect);
        icon4.setImageResource(R.drawable.bb_btn_account_unselect);
    }

    private void changeClickIcon(ImageView iv,int res){
        iv.setImageResource(res);
    }

    /**
     * 改变标题名称
     * @param title
     */
    private void chageMainTitle(String title){
        if (main_title != null){
            main_title.setText(title);
        }
    }


    /**
     * 设置默认的fragment，即//第一次加载界面;
     */
    private void setDefaultFragment(Fragment fm) {
        mFm = getSupportFragmentManager();
        FragmentTransaction mFragmentTrans = mFm.beginTransaction();

        mFragmentTrans.add(R.id.id_fm_content, fm).commit();

        mContent = fm;
    }

    private FragmentManager mFm;
    private Fragment mContent;

    /**
     * 修改显示的内容 不会重新加载 *
     */
    public void switchContent2(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = mFm.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.id_fm_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    /**
     * 修改显示的内容 但会重新加载 *
     */
    public void switchContent(Fragment to){
        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.replace(R.id.id_fm_content,to);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    /**
     * 广播注册
     */
    private void registerReceiver(){
        if(broadReceiver != null) return;
        broadReceiver = new BroadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastAction.BROADCASE_FM_SWITCH);
        filter.addAction(BroadCastAction.BROADCASE_MAIN_TIILE);
        registerReceiver(broadReceiver,filter);
    }

    /**
     * 广播注销
     */
    private void unregisterReceiver(){
        if (broadReceiver == null) return;
        unregisterReceiver(broadReceiver);
        broadReceiver = null;
    }

    private class BroadReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadCastAction.BROADCASE_FM_SWITCH.equals(action)){
                //选择群组或某一监拍机后切换fragment
                initClickIcon();
                switchContent(fm_browse);
            }else if (BroadCastAction.BROADCASE_MAIN_TIILE.equals(action)){
                String title = intent.getStringExtra("main_title");
                chageMainTitle(title);
            }else if(BroadCastAction.BROADCAST_DIALOG_SHOW.equals(action)){
                cancelDialog();
                dialog = new MyLoadingDialog(mContext);
                dialog.show();
            }else if(BroadCastAction.BROADCAST_DIALOG_CANCEL.equals(action)){
                cancelDialog();
            }
        }
    }

    private void cancelDialog(){
        if (dialog != null){
            dialog.cancel();
            dialog = null;
        }
    }

    //记录首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - firstTime > 2000){
                Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }else{
                AppManager.getInstance().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示更新提示对话框
     */
    private void showUpdateDialog() {
        showUpdateDialog2("发现更新","发现更新");
    }

    /**
     * 检查版本更新
     */
    private void checkVersion(){
        HttpURLConnection conn = null;
        try {
            String path = Constants.ApiUrl.UPDATE_CHECK;
            URL realurl = new URL(path);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                info_1 = getUpdataInfo(is);
                if (info_1.equals("") || info_1 == null) {

                } else {
                    if (info_1.getVersion().compareTo(versionname) > 0) {
                        mHandler.sendEmptyMessage(SHOW_UPDATE_VERSION);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xml
     * @param is
     * @return
     * @throws Exception
     */
    public UpdateInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType();
        info = new UpdateInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.getAttributeValue(0));
                    } else if ("url".equals(parser.getName())) {
                        String url = parser.nextText();
                        info.setUrl(url);
                    } else if ("description".equals(parser.getName()) && Locale.getDefault().getLanguage().equals("zh")) {
                        String description = parser.nextText();
                        flag_lg = true;
                        info.setDescription(description);
                        Addtext();
                    } else if ("description_en".equals(parser.getName())) {
                        flag_lg = false;
                        String description_en = parser.nextText();
                        info.setDescription_en(description_en);
                        Addtext();
                    }
                    break;
            }
            type = parser.next();
        }

        return info;
    }

    private void Addtext() {
        View inItemView = mLayoutInflater.inflate(R.layout.addtext, null);
        TextView bodyTv = (TextView) inItemView.findViewById(R.id.tv_context);

        if (Locale.getDefault().getLanguage().equals("zh") && flag_lg) {

            bodyTv.setText(info.getDescription());
        } else if (!Locale.getDefault().getLanguage().equals("zh")) {
            bodyTv.setText(info.getDescription_en());
        }
        ll_text.addView(inItemView);
    }

    public void Remstate(){//保存选择不再提示状态
        if (checkBox1.isChecked()) {
            LoginPrefereces.setState(this, true, "remember");
        } else {
            LoginPrefereces.setState(this, false, "remember");
        }
    }

    public boolean remember(){
        return LoginPrefereces.getState(this,"remember");
    }

    public void showUpdateDialog2(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setView(vi);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(MainActivity.this,UpdateService.class);
                                intent.putExtra("app_name", "jpj_v"+info_1.getVersion());
                                intent.putExtra("url", Constants.ApiUrl.UPDATE_VERSION);
                                startService(intent);
                                Remstate();
                            }
                        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Remstate();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 检查版本线程
     */
    private class UpdateVersionThread extends Thread{
        @Override
        public void run() {
            if(MyApplication.getInstance().checkNetWork() && !remember()){
                checkVersion();
            }
        }
    }


    /**
     * 手动拍照轮询查询
     * @param device_id
     * @param channel_No
     * @param lastTime
     */
    private void pollGetPic(String device_id,String channel_No,String lastTime){
        MyApplication.getInstance().getApi().photographfoGetPic(device_id,channel_No,lastTime,new PollGetPicCallBack(device_id,channel_No));
    }

    private class PollGetPicCallBack extends PanLvApi.ClientAjaxCallback{
        private String device_id;
        private String channel_No;
        public PollGetPicCallBack(String device_id,String channel_No){
            this.device_id = device_id;
            this.channel_No = channel_No;
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);

            String data = ApiErrorCode.getData(t);
            if (data == null) {
                return;
            }

            LogUtil.e("PollGetPicCallBack -- "+data);
            //有最新图片结果  关闭定时器
            PollTimer pollTimer = MyApplication.pollTimerMap.get(device_id+"."+channel_No);
            String device_name = pollTimer.getDevice_name();
            pollTimer.stopTimer();
            JpjPicRecord picRecord = JSON.parseObject(data, JpjPicRecord.class);
            // 发通知
            pollNotif(device_name,picRecord);
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
        }
    }

    /**
     * 轮询通知
     */
    private void pollNotif(String device_name,JpjPicRecord picRecord){
        Intent intent = new Intent(this, PicDetailActivity.class);  //需要跳转指定的页面
        intent.putExtra("JpjPicRecord", picRecord);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContentTitle(device_name+" 新图片")
                .setContentText("点击查看")
                .setSmallIcon(R.drawable.jpj_error)
                .setTicker(device_name+" 抓拍成功");
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        // Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
        // Notification.DEFAULT_SOUND：系统默认铃声。
        // Notification.DEFAULT_VIBRATE：系统默认震动。
        // Notification.DEFAULT_LIGHTS：系统默认闪光。
        builder.setDefaults(Notification.DEFAULT_SOUND);

        notifID++;
        notification.notify(notifID, builder.build());
    }

}
