package com.jsj.member.ob.utils;

import java.util.concurrent.*;


public class ThreadPoolUtils {
    private static ExecutorService exec = new ThreadPoolExecutor(50, 100, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void execute(Runnable command) {
        exec.execute(command);
    }

    // 子线程执行结束future.get()返回null，若没有执行完毕，主线程将会阻塞等待
    public static Future submit(Runnable command) {
        return exec.submit(command);
    }

    // 子线程中的返回值可以从返回的future中获取：future.get();
    public static Future submit(Callable command) {
        return exec.submit(command);
    }
}
