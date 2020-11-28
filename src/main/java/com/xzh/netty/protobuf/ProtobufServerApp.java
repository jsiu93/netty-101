package com.xzh.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ProtobufServerApp {
    private final int port;

    public ProtobufServerApp(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // handle connection
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // handle message IO
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 128) // max keep connect count for boss group
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // keepalive, for worker group
                    .childHandler(new ServerProtobufInitializer());

            ChannelFuture future = bs.bind().sync();
            System.out.println("listening " + future.channel().localAddress());
            future.channel().closeFuture().sync();

        } finally {

            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();

        }
    }

    public static void main(String[] args) throws Exception {
        ProtobufServerApp serverApp = new ProtobufServerApp(8888);
        serverApp.run();
    }
}
