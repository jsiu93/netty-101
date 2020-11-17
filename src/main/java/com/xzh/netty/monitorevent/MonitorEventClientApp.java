package com.xzh.netty.monitorevent;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class MonitorEventClientApp {

    private final String host;
    private final int port;

    public MonitorEventClientApp(String host, int port) {
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
                            socketChannel.pipeline().addLast(new MonitorEventClientHandler());
                        }
                    });
            ChannelFuture future = bs.connect().sync();
            // once connected, send a message to server
//            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello from client", CharsetUtil.UTF_8));

            // blocking operation, closeFuture() open a channel listener, meanwhile all those tasks carry on until the channel disconnect
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }

    public static void main(String[] args) throws Exception {
        new MonitorEventClientApp("127.0.0.1", 8888).run();
    }
}
