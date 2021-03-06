package com.xzh.netty.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * connection monitor
 */
@ChannelHandler.Sharable
public class FixedLengthServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String strInfo = (String) msg;
        System.out.println("receive from client:" + strInfo.trim());
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
        String strMsg = "hello client " + now;
        strMsg = CommonUtil.formatStr(strMsg, CommonUtil.FIXED_LENGTH_FRAME_LENGTH);
        System.out.println("response to client:" + strMsg);

        ctx.writeAndFlush(Unpooled.copiedBuffer(strMsg, StandardCharsets.UTF_8));
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
        ctx.writeAndFlush(Unpooled.copiedBuffer("server channelActive " + now + "\n", StandardCharsets.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("client " + channel.remoteAddress() + "\n" + "offline");
    }
}
