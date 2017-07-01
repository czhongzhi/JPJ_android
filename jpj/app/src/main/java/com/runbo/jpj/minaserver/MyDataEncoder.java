package com.runbo.jpj.minaserver;

import com.runbo.jpj.util.LogUtil;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 编码器将数据直接发出去(不做处理)
 * Created by czz on 2017/4/8.
 */
public class MyDataEncoder extends ProtocolEncoderAdapter {
    private static final String TAG = MyDataEncoder.class.getName();

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        LogUtil.e(TAG + " encode " + session.toString());
        IoBuffer value = (IoBuffer) message;
        out.write(value);
        out.flush();
    }
}
