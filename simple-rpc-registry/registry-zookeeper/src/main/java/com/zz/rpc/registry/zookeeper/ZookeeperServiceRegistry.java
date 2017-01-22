package com.zz.rpc.registry.zookeeper;

import com.zz.rpc.core.constant.Constants;
import com.zz.rpc.core.registry.ServiceRegistry;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZookeeperServiceRegistry implements ServiceRegistry {

    private CuratorFramework client;

    public ZookeeperServiceRegistry(String zkUrl) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        client.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String registryPath = Constants.ZOOKEEPER_REGISTRY_PATH;
        try {
            if (client.checkExists().forPath(registryPath) == null) {
                client.create().forPath(registryPath);
            }
            String servicePath = registryPath + "/" + serviceName;
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().forPath(servicePath);
            }
            String addressPath = servicePath + Constants.ZOOKEEPER_ADDRESS_NODE;
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(addressPath, serviceAddress.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unRegister(String serviceAddress) {
        client.close();
    }
}
