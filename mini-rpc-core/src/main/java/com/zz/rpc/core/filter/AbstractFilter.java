package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;

public abstract class AbstractFilter implements Filter {

    @Override
    public void before(RpcRequest request) {

    }

    @Override
    public void after(RpcResponse response) {

    }
}
