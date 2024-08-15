package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:TestWithCASLockCounter
 * Package:org.example
 * Description:
 *
 * @Date:2024/8/13 14:41
 * @Author:qs@1.com
 */
public class TestWithCASLockCounter {
    private static int count = 0;
    private static final CASLock casLock = new CASLock();
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        /**
         * 线程到达 1000 时，CPU 100%，耗时 68s
         * CAS 方式实现计数器，当线程数较多时，CPU 占用率100%，执行时间较长
         * 高并发下自旋成为了瓶颈
         * 要真正的优化的话可以参考 {@link java.util.concurrent.atomic.LongAdder} 的实现
         *  - 维护一个 Cell[] 数组，线程通过 hash 映射到其中一个 Cell 进行操作（类似于分段锁）
         */
        // CASWithoutOptimize();
        // 这个使用线程池的方式，实际上是将高并发下的自旋转换为了线程池的任务调度，减少了自旋的线程数，本质上来讲还是降低了并发度
        // CASOptimizeWithThreadPool();
        /**
         * 使用 ReentrantLock 代替 CASLock
         * ReentrantLock 使用操作系统的阻塞和唤醒机制，当锁不可用时，线程会被挂起，等待锁可用时被唤醒
         * ReentrantLock 提供了条件变量（Condition），可以实现更复杂的线程间同步机制
          */
        ReentrantLockOptimize();
    }

    private static void ReentrantLockOptimize() {
        long start = System.currentTimeMillis();
        System.out.println("start time: " + System.currentTimeMillis());
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    for (int j = 0; j < 10; j++) {
                        count++;
                        System.out.println(count);
                    }
                    countDownLatch.countDown();
                } finally {
                    lock.unlock();
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("cost time: " + (System.currentTimeMillis() - start));
    }

    private static void CASOptimizeWithThreadPool() {
        int cpu = Runtime.getRuntime().availableProcessors();
        System.out.println("cpu: " + cpu);
        ExecutorService executorService = Executors.newFixedThreadPool(cpu);
        long start = System.currentTimeMillis();
        System.out.println("start time: " + System.currentTimeMillis());

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                casLock.lock();
                try {
                    for (int j = 0; j < 10; j++) {
                        count++;
                        System.out.println(count);
                    }
                } finally {
                    casLock.unlock();
                }
                if (count == 1000) {
                    System.out.println("cost time: " + (System.currentTimeMillis() - start));
                }
            });
        }
        executorService.shutdown();
    }

    private static void CASWithoutOptimize() {
        long start = System.currentTimeMillis();
        System.out.println("start time: " + System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                while (!casLock.cas()) {

                }
                try {
                    for (int j = 0; j < 10; j++) {
                        count++;
                        System.out.println(count);
                    }
                    if (count == 1000) {
                        System.out.println("cost time: " + (System.currentTimeMillis() - start));
                    }
                } finally {
                    casLock.setState(0);
                }

            }).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
