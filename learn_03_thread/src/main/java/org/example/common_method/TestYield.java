package org.example.common_method;

/**
 * ClassName:TestYield
 * Package:org.example.common_method
 * Description: yield 方法
 * 1. 调用 yield 方法会让当前线程从 Running 状态进入 Runnable 状态，让出 CPU 的执行权，让其他线程执行
 * 2. yield 方法不会释放对象锁
 * 3. yield 方法只是让出 CPU 的执行权，让其他线程执行，但是不会阻塞当前线程，所以当前线程有可能会再次抢占 CPU 的执行权
 *
 * @Date:2024/8/9 15:52
 * @Author:qs@1.com
 */
public class TestYield {
    private static Object lock = new Object();
    public static void main(String[] args) {
        // 测试 yield 方法
        // testYield();
        // 测试 yield 方法不会释放对象锁
         testYieldWillNotReleaseObjectLock();
    }

    private static void testYieldWillNotReleaseObjectLock() {
        Thread t = new Thread(() -> {
            synchronized (lock) {
                System.out.println("t线程开始执行");

                Thread.yield();
                for (int i = 0; i < 10000; i++) {
                    System.out.println("t线程正在执行，i = " + i);
                }
                System.out.println("t线程执行结束");
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println("----------> t2线程开始执行");
            synchronized (lock) {
                System.out.println("t2线程获取到锁 >>>>>> 开始执行");
                System.out.println("t2线程执行结束");
            }
        });

        t.start();
        t2.start();

        // 主线程休眠 1 秒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void testYield() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "：正在执行，i = " + i);
                // 当 i = 50 时，让出 CPU 的执行权，让其他线程执行
                if (i == 50) {
                    Thread.yield();
                }
            }
        }, "线程1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "：正在执行，i = " + i);
            }
        }, "线程2");

        t1.start();
        t2.start();
    }
}
