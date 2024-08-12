package org.example.common_method;

/**
 * ClassName:TestJoin
 * Package:org.example.createway.common_method
 * Description: join 方法
 * 1. 目标线程调用 join 方法会让当前线程从 Running 状态进入 Waiting 状态，等待目标线程执行结束才能继续执行
 * 2. join 方法可以传入超时时间，超时时间到达后，当前线程会重新进入 Runnable 状态，等待 CPU 的调度
 * 3. 中断线程会抛出 InterruptedException 异常，会清除中断标记
 *
 * @Date:2024/8/6 17:26
 * @Author:qs@1.com
 */
public class TestJoin {
    public static void main(String[] args) throws InterruptedException {
        // 测试 join 方法
        // testNormalJoin();
        // 测试 join 方法超时
        // testJoinWithTimeout();
        // 测试 join 方法被中断
        testJoinWillBeInterrupted();
    }

    private static void testJoinWillBeInterrupted() {
        Thread mainThread = Thread.currentThread();

        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("t线程被中断");
            }
            System.out.println("t线程执行结束");
        });

        Thread interruptingThread = new Thread(() -> {
            try {
                Thread.sleep(2000); // 等待2秒后中断主线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainThread.interrupt();
            System.out.println("中断主线程完成");
        });

        t.start();
        interruptingThread.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("主线程被中断");
            System.out.println("主线程中断标记：" + Thread.currentThread().isInterrupted());
            System.out.println("t线程中断标记：" + t.isInterrupted());
        }

        System.out.println("主线程执行结束");
    }


    private static void testJoinWithTimeout() {
        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t线程执行结束");
        });

        long start = System.currentTimeMillis();
        t.start();

        System.out.println("主线程开始执行");

        // 主线程等待 t 线程执行结束，最多等待 3 秒
        try {
            t.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程执行结束");
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    private static void testNormalJoin() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t线程执行结束");
        });

        long start = System.currentTimeMillis();
        t.start();

        System.out.println("主线程开始执行");

        // 主线程等待 t 线程执行结束
        t.join();

        System.out.println("主线程执行结束");
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

}
