package com.runbo.jpj.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.entity.JpjUser;
import com.chat.entity.UpdateInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.server.UpdateService;
import com.runbo.jpj.ui.SetAreaActivity;
import com.runbo.jpj.ui.SetPswActivity;
import com.runbo.jpj.ui.SetSexActivity;
import com.runbo.jpj.ui.SetUserActivity;
import com.runbo.jpj.util.AnimImgLoadingListener;
import com.runbo.jpj.util.DataCleanManager;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.ToastUtil;
import com.runbo.jpj.util.UploadTools;
import com.runbo.jpj.view.MyCircleImageView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * 设置
 */
public class SetFragment extends Fragment implements View.OnClickListener {
    private static final int SHOW_UPDATE_VERSION = 100;
    private static final int ITEM1 = Menu.FIRST;
    private static final int ITEM2 = Menu.FIRST + 1;
    //拍照获取头像
    private static final int PIC_FROM_CAMERA = 1010;
    //本地获取
    private static final int PIC_FROM_LOCALPHOTO = 1011;
    private static LinearLayout ll_text;
    private View item_layout;
    private Button out_login;
    private MyCircleImageView ivIcon;//头像
    private RelativeLayout layout;
    private TextView tvName, tvNumber, tvPhone, tvSex, tvArea, tv_cache;
    private String hint;
    private UpdateInfo info, info_1;
    private boolean flag_lg = false;
    private String versionname;
    private LayoutInflater mLayoutInflater;
    private View vi;
    private CheckBox checkBox1;
    private JpjUser userinfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_VERSION://提示是否更新版本
                    showUpdateDialog();
                    break;
            }
        }
    };
    private Uri PhotoUri;//图像剪切
    private Uri CameraUri;//拍照
    private String newName = "icon.jpg";
    private String uploadFile = "";  //图像剪切保存路径
    private UserChangeReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从sqlite数据库获取用户信息
        String userid = LoginPrefereces.getNumber(getContext());
        userinfo = MyApplication.getInstance().DbService.queryJpjUser(userid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        initView(view);
        initData();

        registerReceiver();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void initView(View view) {
        mLayoutInflater = LayoutInflater.from(getContext());
        vi = mLayoutInflater.inflate(R.layout.context_updata, null);
        checkBox1 = (CheckBox) vi.findViewById(R.id.checkBox1);
        ll_text = (LinearLayout) vi.findViewById(R.id.ll_text);


        //头像
        item_layout = view.findViewById(R.id.set_layout_icon);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("头像");
            layout = (RelativeLayout) view.findViewById(R.id.set_layout_icon);
            ivIcon = (MyCircleImageView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(this);
        }

        item_layout = view.findViewById(R.id.set_layout_name);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("用户名");
            tvName = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint = tvName.getText().toString().trim();
                    Intent setuser = new Intent(getContext(), SetUserActivity.class);
                    setuser.putExtra("TITLE_BAR", "name");
                    setuser.putExtra("EditHint", hint);
                    startActivity(setuser);
                }
            });
        }


        item_layout = view.findViewById(R.id.set_layout_account);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("账号");
            tvNumber = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(this);
        }

        item_layout = view.findViewById(R.id.set_layout_phone);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("手机号");
            tvPhone = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint = tvPhone.getText().toString().trim();
                    Intent setphone = new Intent(getContext(), SetUserActivity.class);
                    setphone.putExtra("TITLE_BAR", "phone");
                    setphone.putExtra("EditHint", hint);
                    startActivity(setphone);
                }
            });
        }

        item_layout = view.findViewById(R.id.set_layout_sex);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("性别");
            tvSex = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint = tvSex.getText().toString().trim();
                    Intent setsex = new Intent(getContext(), SetSexActivity.class);
                    //setsex.putExtra("EditHint",hint);
                    setsex.putExtra("EditHint", hint);
                    startActivity(setsex);
                }
            });
        }

        item_layout = view.findViewById(R.id.set_layout_area);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("地区");
            tvArea = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(this);
        }

        //版本更新
        item_layout = view.findViewById(R.id.set_layout_version);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("软件版本");
            versionname = MyApplication.getVersionName(getContext());
            ((TextView) item_layout.findViewById(R.id.set_hitm)).setText(versionname);
            item_layout.setOnClickListener(this);
        }

        //清除缓存
        item_layout = view.findViewById(R.id.set_layout_cache);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("清除缓存");
            try {
                String cache = DataCleanManager.getTotalCacheSize(getContext().getApplicationContext());
                tv_cache = (TextView) item_layout.findViewById(R.id.set_hitm);
                tv_cache.setText(cache);
            } catch (Exception e) {
                e.printStackTrace();
            }
            item_layout.setOnClickListener(this);
        }

        //退出登录
        out_login = (Button) view.findViewById(R.id.out_login);
        out_login.setOnClickListener(this);
    }

    private void initData() {
        if (userinfo == null) return;
        tvName.setText(userinfo.getUsername());
        tvNumber.setText(userinfo.getUserid() + "");
        tvPhone.setText(userinfo.getPhone() == null ? "" : userinfo.getPhone());
        tvSex.setText(userinfo.getSex() == null ? "" : userinfo.getSex());
        tvArea.setText(userinfo.getArea() == null ? "" : userinfo.getArea());
        String headUrl = userinfo.getHeaderUrl();
        if(!TextUtils.isEmpty(headUrl)){
            String picUrl = Constants.ApiUrl.tBASE_PIC_URL+headUrl;
            ImageLoader.getInstance().displayImage(picUrl, ivIcon,
                    MyApplication.options, new AnimImgLoadingListener());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.set_layout_icon://头像设置
                Toast.makeText(getContext(), "头像设置", Toast.LENGTH_SHORT).show();
                registerForContextMenu(layout);
                getActivity().openContextMenu(layout);
                break;
            case R.id.set_layout_account://账号
                Intent setpsw = new Intent(getContext(), SetPswActivity.class);
                startActivity(setpsw);
                break;
            case R.id.set_layout_area://地区
                Intent region = new Intent(getContext(), SetAreaActivity.class);
                startActivity(region);
                break;
            case R.id.set_layout_version://版本更新
                new UpdateVersionThread().start();
                break;

            case R.id.set_layout_cache://清除缓存
                DataCleanManager.clearAllCache(getContext().getApplicationContext());
                tv_cache.setText("0K");
                Toast.makeText(getContext(), "缓存已清除", Toast.LENGTH_SHORT).show();
                break;

            case R.id.out_login://退出登录
                showOutLoginDialog();
                break;
        }
    }

    /**
     * 退出登录提示
     */
    private void showOutLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.mContext);
        builder.setTitle("退出登录：");
        builder.setMessage("确定退出登录吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().delectLocalLogin();
                getActivity().finish();
            }
        });
        builder.create().show();
    }

    /**
     * 广播注册
     */
    private void registerReceiver() {
        if (receiver != null) return;
        receiver = new UserChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BroadCastAction.BROADCAST_CHANGE_USERINFO);
        getContext().registerReceiver(receiver, filter);
    }

    /**
     * 广播注销
     */
    private void unregisterReceiver() {
        if (receiver == null) return;
        getContext().unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, ITEM1, 0, "拍照");
        menu.add(0, ITEM2, 0, "本地");
    }

    //菜单单击响应
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击了拍照
            case ITEM1:
                doHandlerPhoto(PIC_FROM_CAMERA);
                break;
            //点击了本地
            case ITEM2:
                doHandlerPhoto(PIC_FROM_LOCALPHOTO);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 根据不同方式选择图片设置ImageView
     *
     * @param type 0-本地相册选择，非0为拍照
     */
    private void doHandlerPhoto(int type) {
        try {
            //保存剪裁后的图片文件
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            File picFile = new File(pictureFileDir, "icon.jpg");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            //拍照图片文件
            File cameraFileDir = new File(Environment.getExternalStorageDirectory(), "/upload/camera");
            if (!cameraFileDir.exists()) {
                cameraFileDir.mkdirs();
            }
            File camFile = new File(cameraFileDir, "icon.jpg");
            if (!camFile.exists()) {
                camFile.createNewFile();
            }


            PhotoUri = Uri.fromFile(picFile);
            CameraUri = Uri.fromFile(camFile);
            uploadFile = picFile.getAbsolutePath();
            LogUtil.w("MyLog", "选择类型：" + type);
            if (type == PIC_FROM_LOCALPHOTO) {
                //类型本地
                Intent intent = getCropImageIntent();
                startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
            } else {
                //类型拍照
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, CameraUri);
                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用图片剪辑程序
     */
    private Intent getCropImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        setIntentParams(intent);
        return intent;
    }

    /**
     * 设置公用参数
     */
    private void setIntentParams(Intent intent) {

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(CameraUri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("onActivityResult resultCode " + resultCode);
        //判断取消的情况
        if (resultCode == getActivity().RESULT_CANCELED)
            return;
        Bitmap bitmap = null;
        switch (requestCode) {
            case PIC_FROM_CAMERA://拍照
                LogUtil.e("onActivityResult -- PIC_FROM_CAMERA");
                try {
                    cropImageUriByTakePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PIC_FROM_LOCALPHOTO://本地获取
                LogUtil.e("onActivityResult -- PIC_FROM_LOCALPHOTO");
                try {
                    if (PhotoUri != null) {
                        bitmap = decodeUriAsBitmap(PhotoUri);
                        ivIcon.setImageBitmap(bitmap);
                        uploadUserIcon(uploadFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 更新用户头像
     */
    private void uploadUserIcon(final String filePath) {
        LogUtil.e("头像上传地址 - " + filePath);
        if (TextUtils.isEmpty(filePath)) {
            ToastUtil.show(getContext(), "图像为空???");
        }
        new UploadPicAsyncTask().execute(filePath);
    }

    /**
     * 显示更新提示对话框
     */
    private void showUpdateDialog() {
        showUpdateDialog2("发现更新", "发现更新");
    }

    /**
     * 检查版本更新
     */
    private void checkVersion() {
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
     *
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

    public void Remstate() {//保存选择不再提示状态
        if (checkBox1.isChecked()) {
            LoginPrefereces.setState(getContext(), true, "remember");
        } else {
            LoginPrefereces.setState(getContext(), false, "remember");
        }
    }

    public boolean remember() {
        return LoginPrefereces.getState(getContext(), "remember");
    }

    public void showUpdateDialog2(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(vi);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getContext(), UpdateService.class);
                intent.putExtra("app_name", "jpj_v" + info_1.getVersion());
                intent.putExtra("url", Constants.ApiUrl.UPDATE_VERSION);
                getContext().startService(intent);
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

    private class UserChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.BroadCastAction.BROADCAST_CHANGE_USERINFO.equals(action)) {
                //从sqlite数据库获取用户信息
                String userid = LoginPrefereces.getNumber(getContext());
                userinfo = MyApplication.getInstance().DbService.queryJpjUser(userid);
                initData();
            }
        }
    }

    /**
     * 异步上传图片
     */
    private class UploadPicAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            String userid = LoginPrefereces.getNumber(getContext());
            Map<String, String> param = new HashMap<String, String>();
            param.put("user_id", userid);
            param.put("file_type", "jpg");

            String result = UploadTools.uploadFile(path, userid, Constants.ApiUrl.UpdateURL, param);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && result.length() > 0) {
                ToastUtil.show(getContext(), "上传成功");
                if(userinfo != null){
                    userinfo.setHeaderUrl(result);
                    MyApplication.getInstance().DbService.updataJpjUser(userinfo);
                }
            } else {
                ToastUtil.show(getContext(), "上传失败");
            }
        }
    }

    /**
     * 检查版本线程
     */
    private class UpdateVersionThread extends Thread {
        @Override
        public void run() {
            if (MyApplication.getInstance().checkNetWork()) {
                checkVersion();
            }
        }
    }
}
