package com.chuan.nettyio.eventloop;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Jonathan
 */
@Slf4j
public class TestEventLoopTask {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        NioEventLoopGroup nioWorkers = new NioEventLoopGroup(2);
        log.debug("server start...");
        Thread.sleep(2000);
        nioWorkers.execute(()->{
            log.debug("normal task...");
        });
        Future<String> future = nioWorkers.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "chuan";
            }
        });
        String now = future.getNow();
        System.out.println(now);
        String s = future.get();
        System.out.println(s);
    }
}
