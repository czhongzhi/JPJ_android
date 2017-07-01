package com.runbo.jpj.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by czz on 2017/4/1.
 */
public class ToastUtil {

    public static void show(Context c,String msg){
        Toast.makeText(c,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showl(Context c,String msg){
        Toast.makeText(c,msg,Toast.LENGTH_LONG).show();
    }
}
