package com.xzh.netty.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ChannelHandler.Sharable
public class LengthFieldBasedClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        String srcInfoStr = ByteBufUtil.hexDump(in).toUpperCase();
        System.err.println("receive original msg:" + srcInfoStr);

        short header = in.readShort();
        int msgType = in.readByte();
        int contentLength = in.readInt();
        ByteBuf bufContent = in.readBytes(contentLength);
        String content = bufContent.toString(StandardCharsets.UTF_8);
        System.out.println("receive msg:" + content);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        short header = 0x5A5A;
        byte[] msgType = new byte[]{0x01};
        String strContent = "hello world " + now;
        int length = strContent.getBytes().length;

        ctx.write(Unpooled.copyShort(header));
        ctx.write(Unpooled.copiedBuffer(msgType));
        ctx.write(Unpooled.copyInt(length));
        ctx.write(Unpooled.copiedBuffer(strContent, StandardCharsets.UTF_8));

        ctx.flush();
    }


}
