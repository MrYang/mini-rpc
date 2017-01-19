package com.zz.rpc.registry.zookeeper;

import com.zz.rpc.core.registry.ServiceDiscovery;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private CuratorFramework client;

    public ZookeeperServiceDiscovery(String zkUrl) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        client.start();
    }

    @Override
    public String discover(String serviceName) {
        String registryPath = "/rpc_registry";
        String servicePath = registryPath + "/" + serviceName;
        try {
            List<String> addressList = client.getChildren().forPath(servicePath);
            if (!CollectionUtils.isEmpty(addressList)) {
                String address = addressList.get(0);
                return new String(client.getData().forPath(servicePath + "/" + address));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
