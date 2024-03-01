package com.example.thread;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:ScheduledThreadPoolExecutorTest
 * Package:com.example.thread
 * Description: 定时任务线程池源码分析
 * 定时任务线程池的核心参数：核心线程数、最大线程数、线程存活时间、任务队列、拒绝策略
 *
 * @Date:2024/2/28 16:52
 * @Author:qs@1.com
 */
public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String[] queue = new String[10];
        queue[0] = "1";
        queue[1] = "2";
        System.out.printf(queue[0]);

        testScheduledThreadPoolExecutor();
    }

    private static void testScheduledThreadPoolExecutor() throws ExecutionException, InterruptedException {
        // 创建一个定时任务线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        // 通常让 ThreadFactory 创建的线程为守护线程去执行后台任务
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r);
//                thread.setName("自定义守护线程");
//                thread.setDaemon(true);
//                return thread;
//            }
//        });
        ScheduledTask task = new ScheduledTask("任务");
        System.out.println("Created : " + task.getName()+ ", Current Seconds : " + new Date().getSeconds());
        /**
         * 执行流程：
         * 1. task 先添加到延迟队列中
         * 2. 创建线程从延迟队列中取出任务执行
         * 3. 线程空闲后未达到核心线程数，线程不会销毁，park 等待新任务，达到核心线程数后，线程会销毁
         *
         * schedule：延迟 delay 时间后只执行一次任务
         * scheduleWithFixedDelay：第一次执行任务延迟 initialDelay 时间，之后每次执行任务间隔 delay 时间
         *      - 该方式构建的 {@link java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask} 中的 period 为 -delay 时间
         *      - {@link ScheduledThreadPoolExecutor.ScheduledFutureTask#setNextRunTime()} 中计算下次开始执行时间时，使用的是 当前时间 + (-period)
         *      - 也就是说，下次开始执行时间需要上次任务执行完成后，当前时间 + delay 时间 才会执行下次任务
         * scheduleAtFixedRate：第一次执行任务延迟 initialDelay 时间，之后每次执行任务触发时间为上次任务触发时间 + period 时间
         *      - 该方式构建的 {@link java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask} 中的 period 为正数
         *      - {@link ScheduledThreadPoolExecutor.ScheduledFutureTask#setNextRunTime()} 中计算下次开始执行时间时，使用的是 上次任务开始执行时间 + period
         *      - 也就是说，下次执行时间需要上次任务执行完成后，上次任务开始执行时间 + period 时间 才会执行下次任务
         *      - 但是如果任务执行时间超过 period 时间，下次任务的time 还是 lastTime + period < now ，会立即从队列中取出执行不需要再等待了
         */
//        scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);
//         scheduledExecutorService.scheduleWithFixedDelay(task, 1, 2, TimeUnit.SECONDS);
         scheduledExecutorService.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
//         scheduledExecutorService.scheduleAtFixedRate(task, 1, 6, TimeUnit.SECONDS);
    }
}
class ScheduledTask implements Runnable {
    private String name;

    public ScheduledTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public void run() {
        atomicInteger.incrementAndGet();
        System.out.println("executing：" + name + ", Current Seconds : " + new Date().getSeconds());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
