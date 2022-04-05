package com.zz.rpc.core.registry;

/**
 * @author Mr.Yang
 * @since 2017-02-08
 */
public interface Callback {

    /**
     * 添加server
     * @param serverAddress netty服务器提供rpc服务地址
     */
    void add(String serverAddress);

    /**
     * 移除server
     * @param serverAddress netty服务器提供rpc服务地址
     */
    void remove(String serverAddress);
}
