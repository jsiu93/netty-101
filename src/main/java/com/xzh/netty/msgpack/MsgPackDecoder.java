package com.xzh.netty.msgpack;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessagePack messagePack = new MessagePack();

        int length = byteBuf.readableBytes();
        byte[] raw = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), raw, 0, length);

        list.add(messagePack.read(raw, Msg.class));
    }
}
