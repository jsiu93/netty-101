package com.xzh.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgPackEncoder extends MessageToByteEncoder<Msg> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Msg msg, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();

        byte[] raw = messagePack.write(msg);
        byteBuf.writeBytes(raw);

    }
}
