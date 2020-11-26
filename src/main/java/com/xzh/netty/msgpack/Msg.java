package com.xzh.netty.msgpack;

import org.msgpack.annotation.Message;

@Message
public class Msg {
    private short header;

    private byte msgType;

    private String content;


    public short getHeader() {
        return header;
    }

    public void setHeader(short header) {
        this.header = header;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
