package com.chuan.nettyio.project.factory;

import com.chuan.nettyio.project.client.inf.UserService;
import com.chuan.nettyio.project.service.UserServiceMemoryImpl;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
