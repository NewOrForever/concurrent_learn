package com.example.thread;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:NacosClientWorkerTest
 * Package:com.example.thread
 * Description:
 *
 * @Date:2024/3/1 13:31
 * @Author:qs@1.com
 */
public class NacosClientWorkerTest {
    final ScheduledExecutorService executor;
    final ScheduledExecutorService executorService;
//    final ExecutorService executorService;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final AtomicInteger taskIdNumber = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        new NacosClientWorkerTest();
        Thread.sleep(1000000);
    }

    public NacosClientWorkerTest() {
        executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("NacosClientWorkerTest - " + threadNumber.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });

        executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("LongPollingRunnable - " + threadNumber.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
//        executorService = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread t = new Thread(r);
//                t.setName("LongPollingRunnable - " + threadNumber.getAndIncrement());
//                t.setDaemon(true);
//                return t;
//            }
//        });

        /**
         * 1. 3 秒后开始执行，每 10 秒执行一次
         */
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    checkConfigInfo();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }, 3L, 10L, TimeUnit.SECONDS);
    }

    private void checkConfigInfo() {
        int taskId = taskIdNumber.incrementAndGet();
        System.out.println("checkConfigInfo execute by " + Thread.currentThread().getName() + " at " + new Date().getSeconds());
        /**
         * 队列 - 核心线程未满 - addWorker - 执行 task
         *                                                              |-----> LongPollingRunnable.run()
         *                                                              -> executorService.execute(this)
         *                                                                                                  |-----> 加入队列 - 核心线程满了，不做任何操作
         *                                                             |<-------------------------------------------------------|
         *                                                         getTask() - 从队列中取出任务
         *                                                            |-----> 执行 task -> LongPollingRunnable.run()
         *                                                            ...... 这个任务会一直执行下去
         */
        if (taskId <= 3){
            executorService.execute(new LongPollingRunnable(taskId));
        }
    }

    class LongPollingRunnable implements Runnable {
        private int taskId = 0;

        public LongPollingRunnable(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            System.out.println("LongPollingRunnable execute by " + Thread.currentThread().getName() + " at " + new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            executorService.execute(this);
        }
    }
}


