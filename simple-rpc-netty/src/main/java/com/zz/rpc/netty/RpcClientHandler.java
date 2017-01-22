package com.zz.rpc.netty;

import com.zz.rpc.core.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse response;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public RpcResponse getResponse() {
        return response;
    }
}
