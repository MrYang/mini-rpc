package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;

public class LogFilter extends AbstractFilter {

    @Override
    public void before(RpcRequest rpcRequest) {
        System.out.println("before log");
    }

    @Override
    public void after(RpcResponse response) {
        System.out.println("after log");
    }
}
