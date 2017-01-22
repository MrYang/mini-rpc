package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;

public interface Filter {

    void before(RpcRequest rpcRequest);

    void after(RpcResponse response);

    Filter previous();

    void setPrevious(Filter filter);

    Filter next();

    void setNext(Filter filter);
}
