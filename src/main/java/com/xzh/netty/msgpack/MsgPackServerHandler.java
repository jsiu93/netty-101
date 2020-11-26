package com.xzh.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ChannelHandler.Sharable
public class MsgPackServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg in = (Msg) msg;
        System.out.println("received msg:" + in.getContent());

        Msg responseMsg = new Msg();
        responseMsg.setHeader((short) 0x3C3C);
        responseMsg.setMsgType((byte) 0x01);
        responseMsg.setContent("resp from server");

        ctx.writeAndFlush(responseMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("client " + channel.remoteAddress() + " connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("client " + channel.remoteAddress() + " disconnected");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // this method only be invoked once
        Channel incoming = ctx.channel();
        System.out.println("client " + incoming.remoteAddress() + " online");

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        ctx.writeAndFlush(Unpooled.copiedBuffer("server channelActive " + now + "\n", StandardCharsets.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("client " + channel.remoteAddress() + "\n" + "offline");
    }
}
