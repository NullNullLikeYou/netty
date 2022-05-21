package com.chuan.nettyio.promiseAndfuture;

import io.netty.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Jonathan
 */
@Slf4j
public class TestPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AbstractEventExecutor executor = new AbstractEventExecutor() {

            @Override
            public void execute(Runnable command) {
                command.run();
            }

            @Override
            public EventExecutorGroup parent() {
                return null;
            }

            @Override
            public boolean inEventLoop(Thread thread) {
                return false;
            }

            @Override
            public boolean isShuttingDown() {
                return false;
            }

            @Override
            public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public Future<?> terminationFuture() {
                return null;
            }

            @Override
            public void shutdown() {

            }

            @Override
            public boolean isShutdown() {
                return false;
            }

            @Override
            public boolean isTerminated() {
                return false;
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
                return false;
            }
        };
        DefaultPromise<Integer> promise = new DefaultPromise<>(executor);

        executor.execute(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("set success, {}", 10);
            promise.setSuccess(10);
        });

        log.debug("start...");
        // 还没有结果
        log.debug("{}", promise.getNow());
        log.debug("{}", promise.get());
    }
}
