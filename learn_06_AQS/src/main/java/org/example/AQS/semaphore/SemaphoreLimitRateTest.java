package org.example.AQS.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:SemaphoreLimitRateTest
 * Package:org.example.AQS.semaphore
 * Description: Semaphore 信号量限流测试demo
 *
 * @Date:2024/8/31 14:10
 * @Author:qs@1.com
 */
@Slf4j
public class SemaphoreLimitRateTest {
    /**
     * 实现一个限流器，每秒最多允许 5 个请求通过
     */
    private static final Semaphore semaphore = new Semaphore(5);

    /**
     * 定义一个线程池
     * 核心线程数：10
     * 最大线程数：50
     * 空闲的非核心线程存活时间：60s
     * 阻塞队列容量：200
     */
    private static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(10, 50, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(200));

    /**
     * 任务
     */
    private static void runTask() {
        try {
            // 获取一个许可
            semaphore.acquire(1);
            log.debug("线程" + Thread.currentThread().getName() + " - 执行runTask方法");
            // 模拟线程执行任务
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放一个许可
            semaphore.release(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 模拟请求以 10个/s 的速率 -> 限流器会限制每秒最多允许 5 个请求通过
         */
        for (; ; ) {
            Thread.sleep(100);
            executor.execute(() -> runTask());
        }
    }

}
