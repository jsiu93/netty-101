package com.xzh.netty.endecoder;

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
public class LengthFieldBasedServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msgByteBuf = (ByteBuf) msg;
        String originalContent = ByteBufUtil.hexDump(msgByteBuf).toUpperCase();
        System.err.println("receive original msg from client:" + originalContent);

        short headerLength = msgByteBuf.readByte();
        int msgType = msgByteBuf.readUnsignedShort();
        int length = msgByteBuf.readInt();
        ByteBuf byteBuf = msgByteBuf.readBytes(length);
        String content = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("receive msg:" + content);

        short header = 0x3C3C;
        byte[] msgTypeResp =new byte[]{0x01};;
        String respContent = "hello from server";
       int length2 = respContent.getBytes().length;

        ctx.write(Unpooled.copyShort(header));
        ctx.write(Unpooled.copiedBuffer(msgTypeResp));
        ctx.write(Unpooled.copyInt(length2));
        ctx.write(Unpooled.copiedBuffer(respContent, StandardCharsets.UTF_8));

        ctx.flush();

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
