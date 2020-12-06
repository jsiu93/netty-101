package com.xzh.netty.queuetask;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class QueueTaskClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("received object:" + msg);

        InformationProto.Information information = (InformationProto.Information) msg;
        System.out.println("received msg:" + information.getContent());

//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
//                System.out.println("start task..." + now);
//                try {
//                    TimeUnit.SECONDS.sleep(5L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
//                System.out.println("end task..." + now);
//
//            }
//        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
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
                        .setContent("[heartbeat] hello server. " + now)
                        .build();
                ctx.writeAndFlush(information);
            }

            // assign member variable
            public Runnable accept(ChannelHandlerContext ctxx) {
                this.ctx = ctxx;
                return this;
            }
        }.accept(ctx), 0L, 3L, TimeUnit.SECONDS);


    }


}
