package com.zz.rpc.spring.config;

public class ProtocolConfigBean {

    private String id;

    private String name;
    private String serialization;       // 序列化方式

    private String maxContentLength;    // client 请求响应包的最大长度限制
    private int requestTimeout;         // client 请求超时

    private int maxWorkerThread;        // server 最大工作pool线程数
    private int maxServerConnection;    // server 支持的最大连接数

    private String haStrategy;      // 高可用策略
    private String loadbalance;     // 负载均衡策略
    private String cluster;         // 集群策略
    private String proxy;           // jdk, javassist

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(String maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getMaxWorkerThread() {
        return maxWorkerThread;
    }

    public void setMaxWorkerThread(int maxWorkerThread) {
        this.maxWorkerThread = maxWorkerThread;
    }

    public int getMaxServerConnection() {
        return maxServerConnection;
    }

    public void setMaxServerConnection(int maxServerConnection) {
        this.maxServerConnection = maxServerConnection;
    }

    public String getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(String haStrategy) {
        this.haStrategy = haStrategy;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
}
