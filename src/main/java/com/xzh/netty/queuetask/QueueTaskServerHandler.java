package com.xzh.netty.queuetask;

import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class QueueTaskServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InformationProto.Information information = (InformationProto.Information) msg;
        System.out.println("received msg:" + information.getContent());

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));

        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
                System.out.println("start task..." + now);
                try {
                    TimeUnit.SECONDS.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
                System.out.println("end task..." + now);
            }
        });

        byte[] header = new byte[]{0x5A, 0x5A};
        byte[] msgType = new byte[0x01];

        InformationProto.Information resp = InformationProto.Information.newBuilder()
                .setHeader(ByteString.copyFrom(header))
                .setMsgtype(ByteString.copyFrom(msgType))
                .setPersonnum(233)
                .setPrice(0.1f)
                .setContent(information.getContent())
                .build();
        ctx.writeAndFlush(resp);
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

        ctx.executor().scheduleAtFixedRate(new Runnable() {
            ChannelHandlerContext ctx;

            @Override
            public void run() {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
                byte[] header = new byte[]{0x5A, 0x5A};
                byte[] msgType = new byte[0x01];

                InformationProto.Information information = InformationProto.Information.newBuilder()
                        .setHeader(ByteString.copyFrom(header))
                        .setMsgtype(ByteString.copyFrom(msgType))
                        .setPersonnum(233)
                        .setPrice(0.1f)
                        .setContent("[heartbeat from server] hello client. " + now)
                        .build();
                ctx.writeAndFlush(information);
            }

            // assign member variable
            public Runnable accept(ChannelHandlerContext ctxx) {
                this.ctx = ctxx;
                return this;
            }


        }.accept(ctx), 0, 3, TimeUnit.SECONDS);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        ctx.writeAndFlush(Unpooled.copiedBuffer("server channelActive " + now + "\n", StandardCharsets.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("client " + channel.remoteAddress() + "\n" + "offline");
    }
}
