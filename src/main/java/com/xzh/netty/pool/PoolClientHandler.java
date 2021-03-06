package com.xzh.netty.pool;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

@ChannelHandler.Sharable
public class PoolClientHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * customized thread pool for  time-consuming business in a non-blocking way
     * @return
     */
    protected static ExecutorService newFixedThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("netty-biz-%d")
                .setDaemon(true)
                .build();
        return new ThreadPoolExecutor(200, 200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000), threadFactory);
    }

    final static ListeningExecutorService SERVICE = MoreExecutors.listeningDecorator(newFixedThreadPool());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("received object:" + msg);

        InformationProto.Information information = (InformationProto.Information) msg;
        System.out.println("received msg:" + information.getContent());

        SERVICE.submit(new Runnable() {
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
