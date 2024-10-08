package org.example.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * ClassName:CompletableFutureDemo01
 * Package:future
 * Description: 测试 CompletableFuture 的创建异步操作
 * runAsync(Runnable runnable)：无返回值的异步操作，使用默认线程池 ForkJoinPool.commonPool() 作为它的线程池执行异步代码
 * runAsync(Runnable runnable, Executor executor)：无返回值的异步操作，使用指定的线程池执行异步代码
 * supplyAsync(Supplier<U> supplier)：有返回值的异步操作，使用默认线程池 ForkJoinPool.commonPool() 作为它的线程池执行异步代码
 * supplyAsync(Supplier<U> supplier, Executor executor)：有返回值的异步操作，使用指定的线程池执行异步代码
 *
 * @Date:2024/9/30 13:50
 * @Author:qs@1.com
 */
public class CompletableFutureDemo01 {
    public static void main(String[] args) {
        // 无返回值的异步操作，使用默认线程池 ForkJoinPool.commonPool() 作为它的线程池执行异步代码
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "：执行无返回值的异步操作");
        });

        // 无返回值的异步操作，使用指定的线程池执行异步代码
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "：自定义线程池执行无返回值的异步操作");
        }, Executors.newSingleThreadExecutor());

        // 有返回值的异步操作，使用默认线程池 ForkJoinPool.commonPool() 作为它的线程池执行异步代码
        CompletableFuture<String> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "：执行有返回值的异步操作");
            return "--------------> 返回任务结果";
        });

        // 有返回值的异步操作，使用指定的线程池执行异步代码
        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "：自定义线程池执行有返回值的异步操作");
            return "--------------> 返回自定义线程池任务结果";
        }, Executors.newSingleThreadExecutor());

        System.out.println(future01.join());
        System.out.println(future02.join());
    }

}
