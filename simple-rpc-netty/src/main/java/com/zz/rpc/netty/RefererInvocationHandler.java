package com.zz.rpc.netty;

import com.zz.rpc.core.RpcRequest;
import com.zz.rpc.core.RpcResponse;
import com.zz.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class RefererInvocationHandler<T> implements InvocationHandler {

    private AtomicLong atomicLong = new AtomicLong(0);

    private Class<T> clz;

    public RefererInvocationHandler(Class<T> clz) {
        this.clz = clz;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();

        request.setRequestId(atomicLong.getAndIncrement());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setInterfaceName(clz.getName());

        String serviceAddress = new ZookeeperServiceDiscovery("127.0.0.1:2181").discover(clz.getName());
        if (StringUtils.isEmpty(serviceAddress)) {
            throw new RuntimeException("没有找到该服务");
        }
        String host = serviceAddress.split(":")[0];
        int port = Integer.parseInt(serviceAddress.split(":")[1]);
        NettyClient client = new NettyClient(host, port);
        RpcResponse response = client.sendRequest(request);
        return response.getResult();
    }

}
