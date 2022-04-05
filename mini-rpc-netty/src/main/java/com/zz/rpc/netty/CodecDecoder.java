package com.zz.rpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.util.SerializationUtils;

import java.util.List;

public class CodecDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 如果读取的字节数小于消息头, 直接返回
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();  // 标记一下当前的readIndex的位置
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让readIndex增加4
        int dataLength = in.readInt();
        // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
        // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];
        in.readBytes(body);  // 读取消息体
        Object o = SerializationUtils.deserialize(body);
        out.add(o);
    }
}

