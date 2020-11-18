package com.xzh.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import javax.smartcardio.CardTerminal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class HeartbeatClientApp {

    private final String host;
    private final int port;

    public HeartbeatClientApp(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // IO thread pool
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new HeartbeatClientHandler());
                        }
                    });
            ChannelFuture future = bs.connect(); // remove the sync() if you have a listener to add
            future.addListener(new HeartbeatListener());
            // once connected, send a message to server
//            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello from client", CharsetUtil.UTF_8));

            while (true) {
                TimeUnit.SECONDS.sleep(4L);
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
                future.channel().writeAndFlush(Unpooled.copiedBuffer(now + "\r", StandardCharsets.UTF_8));
            }

            // blocking operation, closeFuture() open a channel listener, meanwhile all those tasks carry on until the channel disconnect
//            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }

    public static void main(String[] args) throws Exception {
        new HeartbeatClientApp("127.0.0.1", 8888).run();
    }
}
