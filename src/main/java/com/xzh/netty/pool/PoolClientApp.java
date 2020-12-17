package com.xzh.netty.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class PoolClientApp {

    private final String host;
    private final int port;

    public PoolClientApp(String host, int port) {
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
                    .handler(new ClientPoolInitializer());
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
        new PoolClientApp("127.0.0.1", 8888).run();
    }
}
