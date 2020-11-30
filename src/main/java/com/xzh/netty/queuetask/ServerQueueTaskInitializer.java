package com.xzh.netty.queuetask;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ServerQueueTaskInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer Decoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("Protobuf decoder", new ProtobufDecoder(InformationProto.Information.getDefaultInstance()));
        pipeline.addLast("frame Encoder", new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("Protobuf encoder", new ProtobufEncoder());
        pipeline.addLast("handler", new QueueTaskServerHandler());

    }
}
