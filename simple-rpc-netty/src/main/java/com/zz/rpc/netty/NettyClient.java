package com.zz.rpc.netty;

import com.zz.rpc.core.RpcRequest;
import com.zz.rpc.core.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private String host;
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        RpcClientHandler clientHandler = new RpcClientHandler();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CodecDecoder());
                    ch.pipeline().addLast(new CodecEncoder());
                    ch.pipeline().addLast(clientHandler);
                }
            });

            ChannelFuture future = b.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            return clientHandler.getResponse();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
