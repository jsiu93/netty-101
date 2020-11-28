package com.xzh.netty.protobuf;

import com.xzh.netty.msgpack.MsgPackDecoder;
import com.xzh.netty.msgpack.MsgPackEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ServerProtobufInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer Decoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("Protobuf decoder", new ProtobufDecoder(InformationProto.Information.getDefaultInstance()));
        pipeline.addLast("frame Encoder", new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("Protobuf encoder", new ProtobufEncoder());
        pipeline.addLast("handler", new ProtobufServerHandler());

    }
}
