package com.runbo.jpj.minaserver;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by czz on 2017/4/8.
 */
public class MyCodecFactory implements ProtocolCodecFactory {
    private MyDataDecoder decoder;
    private MyDataEncoder encoder;

    public MyCodecFactory(){
        decoder = new MyDataDecoder();
        encoder = new MyDataEncoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
