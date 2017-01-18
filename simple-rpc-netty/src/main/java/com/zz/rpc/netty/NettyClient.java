package com.zz.rpc.netty;

import com.zz.rpc.core.RpcRequest;
import com.zz.rpc.core.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NettyClient {

    private static boolean close = false;

    private AtomicLong atomicLong = new AtomicLong();

    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        return null;
    }

    public void conect() throws Exception {
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
                        f.channel().eventLoop().scheduleAtFixedRate(() -> {
                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setRequestId(atomicLong.getAndIncrement());
                            rpcRequest.setInterfaceName("helloService");
                            rpcRequest.setMethodName("hello");
                            rpcRequest.setParameterTypes(new Class[]{String.class});
                            rpcRequest.setParameters(new Object[]{"parameter"});
                        }, 0, 1, TimeUnit.SECONDS);
                    } else {
                        future.cause().printStackTrace();
                    }
                }
            });

            while (true) {
                if (close) {
                    break;
                }
            }

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void close() {
        close = true;
    }

}
