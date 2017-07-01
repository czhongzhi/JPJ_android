package com.runbo.jpj.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.entity.JpjPicRecord;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.AnimImgLoadingListener;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.ToastUtil;
import com.runbo.jpj.util.Util;
import com.runbo.jpj.view.MyImageView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;

/**
 * 查看大图
 */
public class PicDetailActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout bar_header;
    private LinearLayout bar_bottom;

    private ImageView bar_back;
    private TextView bar_title, bar_right;
    private PhotoView imageView;

    private TextView id_pic_size,id_pic_gettype,id_pic_pixel,id_pic_time;

    private boolean isEx;//是否展开
    private JpjPicRecord picRecord;
    //private Bitmap mBitmap;

    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    private IWXAPI iwxapi;

    public static final DateFormat Day_Format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);

        picRecord = (JpjPicRecord) getIntent().getSerializableExtra("JpjPicRecord");

        isEx = true;
        initView();

        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.AK_WXCHAT);
        iwxapi.registerApp(Constants.AK_WXCHAT);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initView() {
        bar_header = (RelativeLayout) findViewById(R.id.bar_header);
        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
        imageView = (PhotoView) findViewById(R.id.imageView);

        id_pic_size = (TextView) findViewById(R.id.id_pic_size);
        id_pic_gettype = (TextView) findViewById(R.id.id_pic_gettype);
        id_pic_pixel = (TextView) findViewById(R.id.id_pic_pixel);
        id_pic_time = (TextView) findViewById(R.id.id_pic_time);

        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_right = (TextView) findViewById(R.id.bar_right);//分享
        bar_title = (TextView) findViewById(R.id.bar_title);
        bar_title.setText(picRecord.getUser_name());
        bar_back.setOnClickListener(this);
        bar_right.setOnClickListener(this);
        //imageView.setOnClickListener(this);

        id_pic_size.setText(picRecord.getPictureFileSize()+"k");
        id_pic_gettype.setText((picRecord.getSnapmode() == 0 ? "自动" : "手动"));
        id_pic_pixel.setText(picRecord.getPhotoSize()+"万");

        long time = (long) picRecord.getPictureCaptureDT();
        if(time != 0) {
            time *= 1000;
            String temp = Day_Format.format(new Date(time));
            id_pic_time.setText(temp);
        }

        String picUrl = Constants.ApiUrl.tBASE_PIC_URL + picRecord.getPictureWebURL();
        ImageLoader.getInstance().displayImage(picUrl, imageView,
                MyApplication.options, new AnimImgLoadingListener(this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_back:
                finish();
                break;
            case R.id.bar_right:
                sendWxShareImg();
                //sendWxShareText();
                break;
            case R.id.imageView:
                if (isEx) {
                    startAnim(bar_header, -1, isEx);
                    startAnim(bar_bottom, 1, isEx);
                    isEx = false;
                } else {
                    startAnim(bar_header, -1, isEx);
                    startAnim(bar_bottom, 1, isEx);
                    isEx = true;
                }
                break;
        }
    }

    /**
     * @param view
     * @param i        距离的正负 1 或 -1
     * @param isEx     当前是否是展开状态
     */
    private void startAnim(final View view, int i, final boolean isEx) {
        float vH = view.getHeight();
        TranslateAnimation anim = null;
        vH *= i;
        if (isEx) {
            anim = new TranslateAnimation(0f, 0f, 0f, vH);
        } else {
            anim = new TranslateAnimation(0f, 0f, vH, 0f);
        }
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isEx)
                    view.setVisibility(View.GONE);
                else
                    view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    private void sendWxShareText(){

        String text = "这是个测试";
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = mTargetScene;

        iwxapi.sendReq(req);
    }

    /**
     * 微信分享图片
     */
    private void sendWxShareImg(){
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.jpj_bg);

        Bitmap bitmap = ((BitmapDrawable)((PhotoView)imageView).getDrawable()).getBitmap();

        if(bitmap == null){
            Toast.makeText(this, "详情图还在加载中，请稍等", Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,80,80,true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp,true);
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap = null;
        }

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        iwxapi.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
