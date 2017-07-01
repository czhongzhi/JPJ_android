package com.runbo.jpj.api;

import com.runbo.jpj.constants.Constants;

import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by czz on 2017/3/25.
 */
public class Api extends PanLvApi {

    public Api() {
        super(null);
    }

//    /**
//     * 登录
//     *
//     * @param phone    手机号
//     * @param password 密码  md5
//     * @param callBack
//     */
//    public void login(String phone, String password, ClientAjaxCallback callBack) {
//        AjaxParams params = getParams("login");
//        params.put("phone", phone);
//        params.put("password", password);
//        postPanLv(Constants.ApiUrl.tBASE_USER, params, callBack);
//    }


    /**
     * 获取群组信息
     *
     * @param callBack
     */
    public void queryGroupforJpj(String user_id, ClientAjaxCallback callBack) {
        AjaxParams params = getParams("queryGroupforJpj");
        params.put("user_id", user_id);
        postPanLv(Constants.ApiUrl.tBASE_GROUP, params, callBack);
    }

    /**
     * 获取最新图片
     *
     * @param callBack
     */
    public void queryPicLatestforJpj(String id, String type, ClientAjaxCallback callBack) {
        AjaxParams params = getParams("queryPicLatestforJpj");
        if ("group".equals(type)) {
            params.put("groud_id", id);
        } else if ("device".equals(type)) {
            params.put("device_id", id);
        }
        postPanLv(Constants.ApiUrl.tBASE_GROUP, params, callBack);
    }

    /**
     * 获取历史图片
     */
    public void queryPicHistoryforJpj(String id, String type, long sday, long eday,int page_index,int page_num, ClientAjaxCallback callBack) {
        AjaxParams params = getParams("queryPicHistoryforJpj");
        if ("group".equals(type)) {
            params.put("groud_id", id);
        } else if ("device".equals(type)) {
            params.put("device_id", id);
        }
        params.put("page_index",page_index+"");
        params.put("page_num",page_num+"");
        params.put("sday", sday + "");
        params.put("eday", eday + "");
        postPanLv(Constants.ApiUrl.tBASE_GROUP, params, callBack);
    }

    public void login(String user_id,String password,ClientAjaxCallback callback){
        AjaxParams params = getParams("loginForJpj");
        params.put("user_id", user_id);
        params.put("password", password);
        postPanLv(Constants.ApiUrl.tLogin_URL, params, callback);
    }

    /**
     * 修改用户信息
     */
    public void updataUserinfoForJpj(String user_id, String type, String text, ClientAjaxCallback callback) {
        updataUserinfoForJpj(user_id, type, text, null, callback);
    }

    /**
     * 修改用户密码
     */
    public void updataUserinfoForJpj(String user_id, String type, String oldpsw, String newpsw, ClientAjaxCallback callback) {
        AjaxParams params = getParams("updataUserinfoForJpj");
        if ("name".equals(type)) {//修改用户名
            params.put("name", oldpsw);
        } else if ("password".equals(type)) {//修改密码
            params.put("password", oldpsw);
            params.put("newpsw", newpsw);
        } else if ("phone".equals(type)) {//修改手机号
            params.put("phone", oldpsw);
        } else if ("sex".equals(type)) {//修改性别
            params.put("sex", oldpsw);
        } else if ("area".equals(type)) {//修改地区
            params.put("area", oldpsw);
        }
        params.put("type",type);
        params.put("user_id", user_id);
        postPanLv(Constants.ApiUrl.tBASE_GROUP, params, callback);
    }


    /**
     * 手动拍照
     * @param device_id  设备id
     * @param channel_No 通道号，即：1.主摄像 2.副摄像
     * @param callback
     */
    public void photographfoForJpj(String device_id,String channel_No,ClientAjaxCallback callback){
        AjaxParams params = getParams("photographfoForJpj");
        params.put("device_id",device_id);
        params.put("channel_No",channel_No);
        postPanLv(Constants.ApiUrl.tBASE_GROUP,params,callback);
    }


    /**
     * 通过轮询方式获取手动拍照图片结果（现是临时的方案）
     * @param device_id
     * @param channel_No
     * @param lastTime  当前数据库下手动模式最新图片时间
     * @param callback
     */
    public void photographfoGetPic(String device_id,String channel_No,String lastTime,ClientAjaxCallback callback){
        AjaxParams params = getParams("photographfoGetPic");
        params.put("device_id",device_id);
        params.put("channel_No",channel_No);
        params.put("lastTime",lastTime);
        postPanLv(Constants.ApiUrl.tBASE_GROUP,params,callback);
    }


}
