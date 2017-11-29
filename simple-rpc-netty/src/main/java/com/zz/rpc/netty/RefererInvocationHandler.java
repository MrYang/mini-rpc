package com.zz.rpc.netty;

import com.zz.rpc.core.rpc.RpcParamEnum;
import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;
import com.zz.rpc.core.rpc.RpcServiceParams;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class RefererInvocationHandler implements InvocationHandler {

    private AtomicLong atomicLong = new AtomicLong(0);

    private RpcServiceParams params;
    private List<NettyClient> clients;

    public RefererInvocationHandler(RpcServiceParams params, List<NettyClient> clients) {
        this.params = params;
        this.clients = clients;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();

        request.setRequestId(atomicLong.getAndIncrement());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setInterfaceName(params.getInterfaceName());
        request.addAttachment(RpcParamEnum.version.name(), params.getVersion());
        request.addAttachment(RpcParamEnum.group.name(), params.getGroup());

        NettyClient client = clients.get(new Random().nextInt(clients.size()));
        CompletableFuture<RpcResponse> future = client.sendRequest(request);
        if (future == null) {
            throw new RuntimeException("future is null");
        }
        RpcResponse response = future.get();
        return response.getResult();
    }

}
