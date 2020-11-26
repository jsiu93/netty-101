package com.xzh.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.Inflater;

@ChannelHandler.Sharable
public class MsgPackClientHandler extends SimpleChannelInboundHandler<Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Msg msg) throws Exception {
//        System.out.println("received object:" + msg);
        System.out.println("received content:" + msg.getContent());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Msg msg = new Msg();
        msg.setHeader((short) 0x5A5A);
        msg.setMsgType((byte) 0x01);
        msg.setContent("hello from client");

        System.out.println("msg sent:" + msg.getContent());
        ctx.writeAndFlush(msg);
    }


}
