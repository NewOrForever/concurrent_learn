package com.example.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:ThreadPoolExecutor
 * Package:com.example.thread
 * Description: 线程与线程池执行效率对比
 * 线程池的执行效率高于线程
 *
 *
 *
 * @Date:2024/2/21 15:47
 * @Author:qs@1.com
 */
public class ThreadCompareWithThreadPoolExecutorTest {
    public static final Random random = new Random();
    public static void main(String[] args) throws InterruptedException {
        testThreadCost();
//        testThreadPoolCost();
    }

    /**
     * 测试线程执行效率
     * 1500 ms 左右，10000 个线程
     * 效率低
     */
    public static void testThreadCost() throws InterruptedException {
        final List<Integer> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                list.add(random.nextInt());
                System.out.println(Thread.currentThread().getName() + " is running");
            });
            thread.start();
            thread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("Thread cost time is :" + (end - start));
        System.out.println("List size is :" + list.size());
    }

    /**
     * 测试线程池执行效率
     * 50 ms 左右，10000 个线程
     * 效率高
     */
    public static void testThreadPoolCost() throws InterruptedException {

        final List<Integer> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> {
                list.add(random.nextInt());
                System.out.println(Thread.currentThread().getName() + " is running");
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        long end = System.currentTimeMillis();
        System.out.println("ThreadPool cost time is :" + (end - start));
        System.out.println("List size is :" + list.size());
    }
}
