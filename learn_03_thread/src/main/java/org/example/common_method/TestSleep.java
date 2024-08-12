package org.example.common_method;

/**
 * ClassName:TestSleep
 * Package:org.example.common_method
 * Description: sleep 方法
 * 1. 调用 sleep 方法会让当前线程从 Running 状态进入 Timed Waiting 状态，不会释放对象锁
 * 2. 其它线程可以通过 interrupt 方法打断正在睡眠的线程，会抛出 InterruptedException 异常，会清除中断标记
 * 3. 睡眠时间到达后，线程会重新进入 Runnable 状态，等待 CPU 的调度
 * 4. sleep 传入参数为 0 时，和 yield 方法效果一样，让出 CPU 的执行权，让其他线程执行
 *
 * @Date:2024/8/9 14:45
 * @Author:qs@1.com
 */
public class TestSleep {
    private static final Object lock = new Object();

    public static void main(String[] args) {
        // 测试sleep方法不会释放对象锁
        // testSleepWillNotReleaseObjectLock();
        // 测试sleep方法会被interrupt打断，会清除中断标记
        testSleepWillBeInterrupted();
    }

    private static void testSleepWillBeInterrupted() {
        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("t线程中断标记：" + Thread.currentThread().isInterrupted());
            }
            System.out.println("t线程执行结束");
        });
        t.start();

        // 主线程休眠 1 秒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打断 t 线程
        t.interrupt();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("主线程执行结束");
    }

    private static void testSleepWillNotReleaseObjectLock() {
        Thread t = new Thread(() -> {
            synchronized (lock) {
                System.out.println("t线程开始执行");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t线程执行结束");
            }
        });

        t.start();

        new Thread(() -> {
            System.out.println("-----------> t2线程开始执行");
            synchronized (lock) {
                System.out.println("---------> t2线程获取到对象锁");
            }
        }).start();

        // 主线程休眠 10 秒
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程执行结束");
    }

}
