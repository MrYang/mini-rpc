package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;

public abstract class AbstractFilter implements Filter {

    private Filter next;
    private Filter previous;

    @Override
    public void before(RpcRequest rpcRequest) {

    }

    @Override
    public void after(RpcResponse response) {

    }

    @Override
    public Filter previous() {
        return previous;
    }

    @Override
    public void setPrevious(Filter filter) {
        previous = filter;
    }

    @Override
    public Filter next() {
        return next;
    }

    @Override
    public void setNext(Filter filter) {
        next = filter;
    }
}
