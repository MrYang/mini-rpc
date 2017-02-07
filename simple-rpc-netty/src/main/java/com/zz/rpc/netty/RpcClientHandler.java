package com.zz.rpc.netty;

import com.zz.rpc.core.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private NettyClient nettyClient;

    public RpcClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        if (nettyClient.getFuture(msg.getRequestId()) != null) {
            CompletableFuture<RpcResponse> future = nettyClient.getFuture(msg.getRequestId());
            nettyClient.removeFuture(msg.getRequestId());
            future.complete(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
