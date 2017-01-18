package com.zz.rpc.netty;

import com.zz.rpc.core.RpcRequest;
import com.zz.rpc.core.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) throws Exception {
        /*RpcRequest request = new RpcRequest();
        request.setMethodName("abc");
        new NettyClient().sendRequest(request);*/

        String host = "127.0.0.1";
        int port = 3000;
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
                    ch.pipeline().addLast(new RpcClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("connect success");
                    } else {
                        future.cause().printStackTrace();
                    }
                }
            });


            RpcRequest request = new RpcRequest();
            request.setMethodName("abc");
            f.channel().writeAndFlush(request).sync();
            System.out.println("write request");

            f.channel().closeFuture().sync();
            System.out.println("netty client close...");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        RpcClientHandler clientHandler = new RpcClientHandler();
        String host = "127.0.0.1";
        int port = 3000;
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

            System.out.println("netty client connect...");
            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        f.channel().writeAndFlush(request).sync();
                        System.out.println("netty client response...");
                    }
                }
            });
            f.channel().closeFuture().sync();
            System.out.println("netty client close...");
            return clientHandler.getResponse();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
