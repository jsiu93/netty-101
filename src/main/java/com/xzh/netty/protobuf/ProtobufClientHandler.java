package com.xzh.netty.protobuf;

import com.google.protobuf.ByteString;
import com.xzh.netty.msgpack.Msg;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ChannelHandler.Sharable
public class ProtobufClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        System.out.println("received object:" + msg);

        InformationProto.Information information = (InformationProto.Information) msg;
        System.out.println("received msg:" + information.getContent());

        byte[] header = information.getHeader().toByteArray();
        System.out.println("header:" + ByteBufUtil.hexDump(header).toUpperCase());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
        byte[] header = new byte[]{0x5A, 0x5A};
        byte[] msgType = new byte[0x01];

        InformationProto.Information information = InformationProto.Information.newBuilder()
                .setHeader(ByteString.copyFrom(header))
                .setMsgtype(ByteString.copyFrom(msgType))
                .setPersonnum(233)
                .setPrice(0.1f)
                .setContent("hello server. " + now)
                .build();
        ctx.writeAndFlush(information);

    }


}
