package com.xzh.netty.heartbeat;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

public class HeartbeatListener  implements ChannelFutureListener {

    HeartbeatClientApp app = new HeartbeatClientApp("127.0.0.1", 8888);

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {

        if (!channelFuture.isSuccess()) {
            EventLoop eventLoop = channelFuture.channel().eventLoop();
            eventLoop.schedule(() -> {
                try {
                    System.out.println("trying to connect to server");
                    app.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, 5L, TimeUnit.SECONDS);
        } else {
            System.out.println("connected to server");
        }

    }
}
