package com.chuan.nettyio.factory;

import com.chuan.nettyio.client.inf.UserService;
import com.chuan.nettyio.service.UserServiceMemoryImpl;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
