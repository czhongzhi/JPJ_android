package com.runbo.jpj.api;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.entity.ApiResultVo;

@SuppressLint("UseSparseArrays")
public final class ApiErrorCode {
	/**
	 * 获取数据
	 * @param t
	 * @return
	 * 作者:hurui <br />
	 * 创建时间:2014-5-18<br />
	 * 修改时间:<br />
	 */
	public static String getData(String t){
		try {
			String data = null;
			JSONObject jsonObject = JSON.parseObject(t);
			if(jsonObject.getInteger("error")==0){
				data = jsonObject.getString("data");
			}else{
				data = null;
			}
			return data;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 获取错误码!
	 * @param t
	 * @return
	 * 作者:hurui <br />
	 * 创建时间:2014-5-18<br />
	 * 修改时间:<br />
	 */
	public static boolean getErrorCode(String t){
		if(t == null){
			return false;
		}
		try {
			if(JSON.parseObject(t).getIntValue("error")==0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 获取错误信息的描述
	 * @param t 返回json字符串
	 * @return  错误代号的描述
	 * 作者:hurui <br />
	 * 创建时间:2013-5-21<br />
	 * 修改时间:<br />
	 */
	public static String getErrCodeDescribe(String t){
		try {
			JSONObject jsonObject = JSON.parseObject(t);
			final String msg = jsonObject.getString("msg");
			return msg;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static ApiResultVo getResul(String t){
		try {
			ApiResultVo apiResultVo  = JSON.parseObject(t, ApiResultVo.class);
			return apiResultVo;
		} catch (Exception e) {
			return null;
		}
	}
}
