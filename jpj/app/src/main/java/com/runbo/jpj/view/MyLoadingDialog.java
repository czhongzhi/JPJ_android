package com.runbo.jpj.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;

public class MyLoadingDialog extends Dialog{

	/**返回键是否消失  false 默认不消失  true 消失*/
	private boolean bIsback = true;
	
	public MyLoadingDialog(Context mContext) {
		super(mContext,R.style.MyDialog);
		this.setContentView(R.layout.myloading);
		
	}
	
	public void setBackKey(boolean bIsback){
		this.bIsback = bIsback;
	}

	@Override
	public void cancel() {
		try{
			super.cancel();
		}catch(Exception e){
		}
	}
	
	@Override
	public void dismiss() {
		try{
			super.dismiss();
		}catch(Exception e){
		}
	}
	
	@Override
	public void show() {
		try{
			super.show();
		}catch(Exception e){
		}
	}
	
	@Override
	public boolean isShowing() {
		try{
			super.isShowing();
		}catch(Exception e){
		}
		return false;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && !bIsback) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
