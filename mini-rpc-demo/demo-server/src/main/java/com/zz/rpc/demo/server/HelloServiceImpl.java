package com.zz.rpc.demo.server;

import com.zz.rpc.demo.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello:" + name;
    }
}
