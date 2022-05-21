package com.chuan.nettyio.project.service;

import com.chuan.nettyio.project.client.inf.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
//        int i = 1 / 0;
        return "你好, " + msg;
    }
}