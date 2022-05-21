package com.chuan.nettyio.eventloop;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;

/**
 * @author Jonathan
 */
public class TestEventLoop {
    public static void main(String[] args) {
        DefaultEventExecutorGroup executorGroup = new DefaultEventExecutorGroup(2);
        for (EventExecutor eventExecutor : executorGroup) {
            System.out.println(eventExecutor);
        }
    }
}
