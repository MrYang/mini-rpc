package com.zz.rpc.demo.server;

import com.zz.rpc.demo.api.HelloService2;

public class HelloService2Impl implements HelloService2 {
    @Override
    public String hello2(String name) {
        return "hello2:" + name;
    }
}
