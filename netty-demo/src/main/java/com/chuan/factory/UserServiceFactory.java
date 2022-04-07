package com.chuan.factory;

import com.chuan.client.inf.UserService;
import com.chuan.service.UserServiceMemoryImpl;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
