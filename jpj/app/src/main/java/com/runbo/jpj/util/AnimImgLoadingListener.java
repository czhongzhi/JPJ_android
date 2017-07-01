package com.runbo.jpj.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.runbo.jpj.view.MyLoadingDialog;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by czz on 2017/3/25.
 */
public  class AnimImgLoadingListener extends SimpleImageLoadingListener {

    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
    private Context context;
    private MyLoadingDialog dialog;
    private boolean isShowDialog;

    public AnimImgLoadingListener(){
        isShowDialog = false;
    }

    public AnimImgLoadingListener(Context context){
        this.context = context;
        isShowDialog = true;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        super.onLoadingStarted(imageUri, view);
        if(isShowDialog){
            dialog = new MyLoadingDialog(context);
            dialog.show();
        }
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        super.onLoadingFailed(imageUri, view, failReason);
        cancel();
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        super.onLoadingCancelled(imageUri, view);
        cancel();
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        cancel();
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        }
    }

    private void cancel(){
        if(dialog != null){
            dialog.cancel();
            dialog = null;
        }
    }
}
