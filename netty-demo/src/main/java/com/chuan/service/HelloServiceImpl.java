package com.chuan.service;

import com.chuan.client.inf.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
//        int i = 1 / 0;
        return "你好, " + msg;
    }
}