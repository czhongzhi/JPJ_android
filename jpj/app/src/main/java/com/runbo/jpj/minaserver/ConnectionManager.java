package com.runbo.jpj.minaserver;

import android.content.Context;
import android.content.Intent;

import com.runbo.jpj.util.LogUtil;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by czz on 2017/4/5.
 */
public class ConnectionManager {
    private static final String BROABCAST_ACTION = "com.runbo.jpj.ssss";
    private static final String MESSAGE = "message";
    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;

    private NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;
    private String localIP;

    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<Context>(config.getContext());
        init();
    }

    private void init() {
        //mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnection = new NioSocketConnector();
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());

        mConnection.getSessionConfig().setReaderIdleTime(5 * 60);
        mConnection.getSessionConfig().setWriterIdleTime(5 * 60);
        mConnection.getSessionConfig().setBothIdleTime(5 * 60);

        mConnection.getFilterChain().addFirst("reconn",new MyIoFilterAdapter());
        //mConnection.getFilterChain().addLast("mycoder",new ProtocolCodecFilter(new MyCodecFactory()));

        //mConnection.getFilterChain().addLast("logging", new LoggingFilter());
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        mConnection.setHandler(new DefaultHandler(mContext.get()));
        //mConnection.setDefaultRemoteAddress(mAddress);
        localIP = getIPAddress(true);
    }

    public boolean connect() {
        LogUtil.e("mina 准备连接");
        try {
            LogUtil.e("localIP "+localIP);
            InetSocketAddress remoteAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
            InetSocketAddress localAddress = new InetSocketAddress(localIP,55555);

            ConnectFuture future = mConnection.connect(remoteAddress,localAddress);
            future.awaitUninterruptibly();
            mSession = future.getSession();

            if (mSession != null && mSession.isConnected()) {
                SessionManager.getInstance().setSession(mSession);
            } else {
                LogUtil.e("连接失败 1");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("连接失败 2");
            return false;
        }
        return true;
    }

    public void disContect() {
        mConnection.dispose();
        mConnection = null;
        mSession = null;
        mAddress = null;
        mContext = null;
        LogUtil.e("断开连接");
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private class DefaultHandler extends IoHandlerAdapter {
        private Context mContext;

        private DefaultHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            LogUtil.e("mina -- sessionCreated" + session.toString());
            super.sessionCreated(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            LogUtil.e("mina -- sessionOpened" + session.toString());
            super.sessionOpened(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            LogUtil.e("mina -- sessionIdle" + session.toString());
            super.sessionIdle(session, status);
//            if(session != null){
//                session.closeOnFlush();
//            }
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            LogUtil.e("mina -- sessionClosed" + session.toString());
            super.sessionClosed(session);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            LogUtil.e("mina -- exceptionCaught" + session.toString());
            super.exceptionCaught(session, cause);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            LogUtil.e("mina -- messageSent" + session.toString());
            super.messageSent(session, message);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            //super.messageReceived(session, message);
            LogUtil.e("mina -- messageReceived " + message.toString() +" || " +session.toString());
            if (mContext != null) {
                Intent intent = new Intent(BROABCAST_ACTION);
                intent.putExtra(MESSAGE, message.toString());
                mContext.sendBroadcast(intent);
            }
        }
    }

    private class MyIoFilterAdapter extends IoFilterAdapter{
        @Override
        public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
            //super.sessionClosed(nextFilter, session);
            while (true){
                if (mConnection == null){
                    break;
                }
                SessionManager.getInstance().setSession(null);
                if (ConnectionManager.this.connect()){
                    LogUtil.e("断线重连[" + mConnection.getDefaultRemoteAddress().getHostName() + ":" +
                            mConnection.getDefaultRemoteAddress().getPort() + "]成功");
                    break;
                }
                Thread.sleep(5000);
            }
        }
    }
}
