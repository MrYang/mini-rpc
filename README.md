# simple rpc


参考微博[motan](https://github.com/weibocom/motan/blob/master/docs/wiki/zh_quickstart.md)及dubbo框架实现

## 系统依赖

- JDK1.8
- zookeeper3.4.9

## 简单调用

参见 simple-rpc-demo 模块

server:

```xml
<!-- 标明是server 端,并在3000端口监听服务 -->
<rpc:server port="3000"/>
<!-- 把服务注册到zookeeper 上 -->
<rpc:registry protocol="zookeeper" address="127.0.0.1:2181"/>

<bean id="helloServiceImpl" class="com.zz.rpc.demo.server.HelloServiceImpl"/>
<!-- 导出服务 -->
<rpc:service interfaceClass="com.zz.rpc.demo.api.HelloService" ref="helloServiceImpl"/>
```

client:

```xml
<!-- 标明是client 端 -->
<rpc:client/>
<!-- 从zookeeper 上获取服务 -->
<rpc:registry protocol="zookeeper" address="127.0.0.1:2181"/>
<!-- 引用服务 -->
<rpc:referer interfaceClass="com.zz.rpc.demo.api.HelloService" id="helloService"/>
```

分别运行DemoServer,DemoClient 类即可看到远程调用效果