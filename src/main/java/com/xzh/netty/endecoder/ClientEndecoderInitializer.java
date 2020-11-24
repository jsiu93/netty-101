package com.xzh.netty.endecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientEndecoderInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("framer", new FixedLengthFrameDecoder(CommonUtil.FIXED_LENGTH_FRAME_LENGTH));
//        pipeline.addLast("framer", new LineBasedFrameDecoder(CommonUtil.LINE_BASED_FRAME_LENGTH, true, true));
//        ByteBuf delimiter = Unpooled.copiedBuffer("$".getBytes());
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(CommonUtil.DELIMITER_BASED_FRAME_LENGTH, true, true, delimiter));
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(2048, 3, 4, 0, 0));

//        pipeline.addLast("decoder", new StringDecoder());
//        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast(new LengthFieldBasedClientHandler());
    }
}
