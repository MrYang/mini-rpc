package com.zz.rpc.netty;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private String host;
    private int port;

    private ConcurrentMap<Long, CompletableFuture<RpcResponse>> futureMap = new ConcurrentHashMap<>();
    private Channel channel;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel getChannel() {
        if (channel == null) {
            synchronized (this) {
                if (channel == null) {
                    connect();
                }
            }
        }
        return channel;
    }

    private void connect() {
        Bootstrap b = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        RpcClientHandler clientHandler = new RpcClientHandler(this);
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CodecDecoder());
                        ch.pipeline().addLast(new CodecEncoder());
                        ch.pipeline().addLast(clientHandler);
                    }
                });

        try {
            ChannelFuture channelFuture = b.connect(host, port).sync();
            CompletableFuture<Channel> future = new CompletableFuture<>();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        future.complete(channelFuture.channel());
                    }
                }
            });
            this.channel = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<RpcResponse> sendRequest(RpcRequest request) throws Exception {
        CompletableFuture<RpcResponse> cf = new CompletableFuture<>();
        futureMap.put(request.getRequestId(), cf);
        Channel channel = getChannel();
        ChannelFuture writeFuture = channel.writeAndFlush(request);
        boolean result = writeFuture.awaitUninterruptibly(5, TimeUnit.SECONDS);
        if (result && writeFuture.isSuccess()) {
            return cf;
        }
        return null;
    }

    public CompletableFuture<RpcResponse> getFuture(Long requestId) {
        return futureMap.get(requestId);
    }

    public void removeFuture(Long requestId) {
        futureMap.remove(requestId);
    }
}
