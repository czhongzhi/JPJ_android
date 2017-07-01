package com.runbo.jpj.api;


import java.util.Map;

import android.text.TextUtils;

import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.util.LogUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class PanLvApi {
	protected static FinalHttp finalHttp = new FinalHttp();
	private String actionUrl;
	
	/** 是否开启DEBUG 模式 */
	protected boolean deBug = true;
	
	public PanLvApi(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	protected void postPanLv(AjaxParams params, ClientAjaxCallback callBack) {
		postPanLv(actionUrl, params, callBack);
	}
	
	protected void postPanLv(String url, AjaxParams params, ClientAjaxCallback callBack) {
		LogUtil.e(url+"?"+params.toString());
		
		if(!MyApplication.getInstance().checkNetWork()){
			return;
		}
		finalHttp.configTimeout( 60 * 1000 );
		finalHttp.post(url, params, callBack);
	}
	
	protected void getPanLv(String url,ClientAjaxCallback callBack){
		LogUtil.e( "getPanLv:" + url);	

		if(!MyApplication.getInstance().checkNetWork()){
			return;
		}		
		finalHttp.configTimeout( 60 * 1000 );		
		finalHttp.get(url, callBack);
	}
	
	protected void attribleEmpty(String attrible) {
		if (TextUtils.isEmpty(attrible)) {
			throw new NullPointerException("uid attribute cannot be empty!");
		}
	}

	protected void attribleEmpty(Object attrible) {
		if (null == attrible) {
			throw new NullPointerException("attribute cannot be empty!");
		}
	}


	protected AjaxParams getParams(String action) {
		AjaxParams params = new AjaxParams();
		params.put("action", action);
		return params;
	}
	
	protected AjaxParams getParams(String action, String uid) {
		attribleEmpty(uid);
		AjaxParams params = getParams(action);
		params.put("uid", uid);
		return params;
	}
	
	protected String getUid(int uid){
		try {
			return String.valueOf(uid);
		} catch (Exception e) {
			return "-1";
		}
	}
	
	protected String getstr(Double num){
		try {
			return String.valueOf(num);
		} catch (Exception e) {
			return "-1";
		}
	}
	
	protected String getGroupId(int groupid){
		try {
			return String.valueOf(groupid);
		} catch (Exception e) {
			return "-1";
		}
	}
	
	public FinalHttp getFinalHttp() {
		return finalHttp;
	}

	protected AjaxParams getParams(Map<String, String> maps, String action) {
		AjaxParams params = new AjaxParams(maps);
		params.put("action", action);
		return params;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public static void closeClient(){
		finalHttp.getHttpClient().getConnectionManager().closeExpiredConnections();
	}
	
	public static class ClientAjaxCallback extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String t) {
			closeClient();
			super.onSuccess(t);
		}
		
		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			closeClient();
			LogUtil.e("errorNo:"+errorNo+"   strMsg:"+strMsg);
			super.onFailure(t, errorNo, strMsg);
			t.printStackTrace();
		}

	}
}
