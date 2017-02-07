package com.zz.rpc.netty;

import com.zz.rpc.core.filter.FilterProcessor;
import com.zz.rpc.core.registry.ServiceDiscovery;
import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class RefererInvocationHandler<T> implements InvocationHandler {

    private AtomicLong atomicLong = new AtomicLong(0);

    private Class<T> clz;
    private ServiceDiscovery serviceDiscovery;
    private List<NettyClient> clients;

    public RefererInvocationHandler(Class<T> clz, ServiceDiscovery serviceDiscovery, List<NettyClient> clients) {
        this.clz = clz;
        this.serviceDiscovery = serviceDiscovery;
        this.clients = clients;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();

        request.setRequestId(atomicLong.getAndIncrement());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setInterfaceName(clz.getName());

        String serviceAddress = serviceDiscovery.discover(clz.getName());
        if (StringUtils.isEmpty(serviceAddress)) {
            throw new RuntimeException("没有找到该服务");
        }
        FilterProcessor.before(request);
        NettyClient client = clients.get(new Random().nextInt(clients.size()));
        CompletableFuture<RpcResponse> future = client.sendRequest(request);
        if (future == null) {
            throw new RuntimeException("future is null");
        }
        RpcResponse response = future.get();
        FilterProcessor.after(response);
        return response.getResult();
    }

}
