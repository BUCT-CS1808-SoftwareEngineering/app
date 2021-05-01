package cn.edu.buct.se.cs1808.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppThreadPool {
    private final static int CORE_SIZE = 4;
    private final static int MAX_POOL_SIZE = 6;
    private final static int ALIVE_TIME = 0;
    private final static int QUEUE_SIZE = 16;

    private static ThreadPoolExecutor threadPoolExecutor;
    private AppThreadPool() {}
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            synchronized (AppThreadPool.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(CORE_SIZE, MAX_POOL_SIZE, ALIVE_TIME, TimeUnit.SECONDS,
                                                                new ArrayBlockingQueue<>(QUEUE_SIZE),
                                                                new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return threadPoolExecutor;
    }

}
