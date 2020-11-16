package com.xzh.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ServerApp {
    private final int port;

    public ServerApp(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = bs.bind().sync();
            System.out.println("listening " + future.channel().localAddress());
            future.channel().closeFuture().sync();

        } finally {

            group.shutdownGracefully().sync();

        }
    }

    public static void main(String[] args) throws Exception {
        ServerApp serverApp = new ServerApp(8888);
        serverApp.run();
    }
}
