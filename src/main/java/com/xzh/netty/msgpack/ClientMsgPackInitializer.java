package com.xzh.netty.msgpack;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ClientMsgPackInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
        pipeline.addLast("MessagePack decoder", new MsgPackDecoder());
        pipeline.addLast("prepender", new LengthFieldPrepender(2));
        pipeline.addLast("MessagePack encoder", new MsgPackEncoder());
        pipeline.addLast("handler", new MsgPackClientHandler());
    }
}
