package com.xiekun.dealer.handler.handler.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerThreadPoolConfig {

    // private static final String PRE_FIX = "dealer.";

    public static ThreadPoolExecutor getExecutor() {
        return new ThreadPoolExecutor(1, 10,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
