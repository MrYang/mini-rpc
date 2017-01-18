package com.zz.rpc.netty;

import com.zz.rpc.core.RpcRequest;
import com.zz.rpc.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest req) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(req.getRequestId());
        String interfaceName = req.getInterfaceName();
        String methodName = req.getMethodName();
        Class<?>[] parameterTypes = req.getParameterTypes();
        Object[] parameters = req.getParameters();

        Object object = serviceMap.get(interfaceName);
        Method method = ClassUtils.getMethod(object.getClass(), methodName, parameterTypes);
        Object result = method.invoke(object, parameters);
        rpcResponse.setResult(result);
        ctx.writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
