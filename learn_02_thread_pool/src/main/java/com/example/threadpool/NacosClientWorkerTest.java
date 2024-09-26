package com.example.threadpool;

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
         * 1. 3 秒后开始执行，每 5 秒执行一次
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
        }, 3L, 5L, TimeUnit.SECONDS);
    }

    private void checkConfigInfo() {
        int taskId = taskIdNumber.incrementAndGet();
        System.out.println("checkConfigInfo execute by " + Thread.currentThread().getName() + " at " + new Date().getSeconds());
        /**
         * 长轮询任务的线程池核心线程数为当前服务器的 CPU 核心数 - 16 (我的电脑是 16核心)
         * 长轮询任务的原理，以及每次重新提交自己后当前线程在何时释放：
         * task
         *   |-----> 加入延迟队列
         *                        |-----> addWorker 创建线程 Thread01
         *                                                         |-----> Thread01 执行 task -> LongPollingRunnable.run()
         *                                                         -> executorService.execute(this) 重新提交自己到线程池
         *                                                                                           |-----> 加入延迟队列
         *                                                                                                                  |-----> addWorker 创建线程 Thread02
         *                                                                                                                  =======> Thread02.start() 新线程 runWorker 中从延迟队列中取出任务执行
         *                                                                                                                  |-----> Thread02.start() 线程2 start 时，Thread01的任务就完成了
         *                                                                                                                  |=======> Thread01 线程释放，去延迟队列中和别的线程抢任务
         *
         */
        if (taskId <= 3){
            System.out.println("第 " + taskId + " 次执行checkConfigInfo任务");
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


