package com.zz.rpc.core.registry;

public interface ServiceRegistry {

    void register(String serviceName, String serviceAddress);
}