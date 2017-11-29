package com.zz.rpc.netty;

import com.zz.rpc.core.rpc.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest req) throws Exception {
        System.out.println("requestId:" + req.getRequestId());

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(req.getRequestId());
        String interfaceName = req.getInterfaceName();
        String methodName = req.getMethodName();
        Class<?>[] parameterTypes = req.getParameterTypes();
        Object[] parameters = req.getParameters();

        Map<String, String> attachments = req.getAttachments();
        String version = attachments.get(RpcParamEnum.version.name());
        String group = attachments.get(RpcParamEnum.group.name());
        String key = RpcServiceParams.key(version, group, interfaceName);
        Object object = RpcServiceUtils.serviceMap.get(key);
        Method method = ClassUtils.getMethod(object.getClass(), methodName, parameterTypes);
        try {
            Instant start = Instant.now();
            Object result = method.invoke(object, parameters);
            rpcResponse.setProcessTime(Duration.between(start, Instant.now()).toMillis());
            rpcResponse.setResult(result);
        } catch (Exception e) {
            rpcResponse.setException(e);
        }

        ctx.writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
