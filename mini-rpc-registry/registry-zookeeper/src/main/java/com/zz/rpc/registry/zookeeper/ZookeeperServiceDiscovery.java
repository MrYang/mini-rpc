package com.zz.rpc.registry.zookeeper;

import com.zz.rpc.core.constant.Constants;
import com.zz.rpc.core.registry.Callback;
import com.zz.rpc.core.registry.ServiceDiscovery;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private CuratorFramework client;

    public ZookeeperServiceDiscovery(String zkUrl) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        client.start();
    }

    @Override
    public String discover(String serviceName, Callback callback) {
        String registryPath = Constants.ZOOKEEPER_REGISTRY_PATH;
        String servicePath = registryPath + "/" + serviceName;
        PathChildrenCache watcher = new PathChildrenCache(client, servicePath, true);
        watcher.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    callback.add(new String(event.getData().getData()));
                    break;
                case CHILD_REMOVED:
                    callback.remove(new String(event.getData().getData()));
                    break;
                default:
                    break;
            }
        });
        try {
            watcher.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<String> serviceList = new ArrayList<>();
        try {
            List<String> addressList = client.getChildren().forPath(servicePath);
            if (!CollectionUtils.isEmpty(addressList)) {
                for (String address : addressList) {
                    serviceList.add(new String(client.getData().forPath(servicePath + "/" + address)));
                }
                return String.join(",", serviceList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("没有找到提供者");
    }
}
