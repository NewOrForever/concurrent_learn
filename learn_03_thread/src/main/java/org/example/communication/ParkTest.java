package org.example.communication;

import java.util.concurrent.locks.LockSupport;

/**
 * ClassName:ParkTest
 * Package:org.example.common_method.communication
 * Description: java 线程间通信之 park/unpark 等待唤醒机制
 * 1. park 方法会让当前线程从 Running 状态进入 Waiting 状态，不会释放对象锁，unpark 方法会让线程进入 Runnable 状态
 * 2. unpark 方法会唤醒一个正在等待的线程，可以指定唤醒哪个线程
 * 3. park 方法可以响应中断，但是不会抛出 InterruptedException 异常
 * 4. park 方法可以传入超时时间，超时时间到达后，线程会重新进入 Runnable 状态，等待 CPU 的调度
 * 5. park 方法可以传入 blocker 对象，用于监控线程的阻塞情况
 *
 * @Date:2024/8/12 15:46
 * @Author:qs@1.com
 */
public class ParkTest {
    private static final Object lock = new Object();
    private static boolean flag = true;

    public static void main(String[] args) {
        // 测试 park/unpark 方法
        // testParkUnpark();
        // 测试 park 不会释放对象锁
        // testParkWillNotReleaseObjectLock();
        // 测试 park 方法可以响应中断
        testParkCanRespondToInterrupt();
    }

    private static void testParkCanRespondToInterrupt() {
        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                LockSupport.park();
                System.out.println("---------> t线程中断成功");
                System.out.println("t线程执行结束");
            } catch (Exception e) {
                /**
                 * park 方法可以响应中断，但是不会抛出 InterruptedException 异常
                 * 这里不会捕获到异常，因为 park 方法不会抛出异常，只会响应中断，所以这里不会执行
                 */
                e.printStackTrace();
                System.out.println("t线程中断标记：" + Thread.currentThread().isInterrupted());
            }
        });
        t.start();

        // 主线程休眠 1 秒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打断 t 线程
        System.out.println("start interrupt t ......");
        t.interrupt();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("主线程执行结束");
    }

    private static void testParkUnpark() {
        Thread parkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("park start .......");
                LockSupport.park();
                System.out.println("parkUntil 开始执行，超时时间 3 秒");
                LockSupport.parkUntil(System.currentTimeMillis() + 3000);
                System.out.println("park end .......");
            }
        });
        parkThread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("unpark start .......");
        LockSupport.unpark(parkThread);
    }

    private static void testParkWillNotReleaseObjectLock() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (flag) {
                    System.out.println("park start .......");
                    LockSupport.park();
                    // LockSupport.parkNanos(1000000000);
                    // LockSupport.parkUntil(System.currentTimeMillis() + 1000);
                    // LockSupport.park("blocker");
                    // LockSupport.park("blocker", 123);
                }
                System.out.println("park end .......");
            }
        });
        t1.start();

        new Thread(() -> {
            System.out.println("unpark thread 开始执行");
            if (flag) {
                synchronized (lock) {
                    if (flag) {
                        // unpark 方法会唤醒一个正在等待的线程，可以指定唤醒哪个线程
                        LockSupport.unpark(t1);
                        System.out.println("unpark .......");
                        flag = false;
                    }
                }
            }
        }).start();
    }

}
