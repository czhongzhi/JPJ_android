package com.runbo.jpj.minaserver;

import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.util.LogUtil;

import org.apache.mina.core.session.IoSession;

/**
 * Created by czz on 2017/4/5.
 */
public class SessionManager {

    private static SessionManager mInstance = null;

    private IoSession mSession;

    public static SessionManager getInstance(){
        if(mInstance == null){
            synchronized (SessionManager.class){
                if(mInstance == null){
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager(){}

    public void setSession(IoSession session){
        this.mSession = session;
    }

    public void writeToServer(Object msg){
        if(mSession != null){
            LogUtil.e("writeToServer mSession - " + mSession.toString());
            LogUtil.e(mSession.getLocalAddress()+ " || " + mSession.getRemoteAddress());
            LogUtil.e("客户端准备发送消息");
            mSession.write(msg);
        }
    }

    public void closeSession(){
        if(mSession != null){
            mSession.closeOnFlush();
        }
    }

    public void removeSession(){
        this.mSession = null;
    }

}
