package com.runbo.jpj.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.runbo.jpj.R;
import com.runbo.jpj.util.FileUtil;
import com.runbo.jpj.util.LogUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;


/***
 * 更新版本
 */
public class UpdateService extends Service {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    private String down_url = "";
    private String app_name;
    private String filePath;

    private static final int NOTIFY_ID = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            app_name = intent.getStringExtra("app_name");
            down_url = intent.getStringExtra("url");
            filePath= Environment.getExternalStorageDirectory()+ "/" + app_name + ".apk";

            downApk(down_url,filePath);
        }catch (Exception e){
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void downApk(String url, String filePath) {
        FinalHttp finalHttp = new FinalHttp();
        HttpHandler<File> httpHandler = finalHttp.download(url, filePath, new AjaxCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.w("version updata start");
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                builder = new NotificationCompat.Builder(getBaseContext());
                builder.setContentTitle("版本更新")
                        .setContentText("正在下载......0%")
                        .setSmallIcon(R.drawable.jpj_error);
                builder.setTicker("下载版本更新");

                mNotificationManager.notify(NOTIFY_ID, builder.build());
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                double a = count;
                double b = current;
                int currentPro = (int) ((b / a) * 100);
                builder.setProgress(100,  currentPro, false);
                builder.setContentText("正在下载......"+currentPro+"%");
                mNotificationManager.notify(NOTIFY_ID, builder.build());

                LogUtil.w("version updataing -- " +currentPro);
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                builder.setContentText("下载完成，点击安装").setProgress(0, 0, false);

                LogUtil.w("version updata success");

                // 下载完成，点击安装
                Uri uri = Uri.fromFile(FileUtil.createFile(getBaseContext(), app_name));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"application/vnd.android.package-archive");

                PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                builder.setDefaults(Notification.DEFAULT_SOUND);

                builder.setAutoCancel(true);

                mNotificationManager.notify(NOTIFY_ID, builder.build());
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mNotificationManager.cancel(NOTIFY_ID);
                LogUtil.w("version updata failure");
            }
        });
    }
}
