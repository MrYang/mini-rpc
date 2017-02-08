package com.zz.rpc.core.registry;

/**
 * @author Mr.Yang
 * @since 2017-02-08
 */
public interface Callback {

    void add(String clientAddress);
    void remove(String clientAddress);
}
