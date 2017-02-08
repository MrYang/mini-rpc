package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;

public interface Filter {

    void before(RpcRequest request);

    void after(RpcResponse response);
}
