package org.example.common_method;

/**
 * ClassName:TestStop
 * Package:org.example.common_method
 * Description: stop 方法
 * 1. stop 方法会立即终止线程，不会保证线程的资源正常释放，可能会导致线程的状态不一致
 * 2. stop 方法已经被废弃，不建议使用
 * 3. stop 方法会释放对象锁，可能会导致数据不一致
 * 4. stop 方法会抛出 ThreadDeath 错误，不会抛出异常，所以无法捕获
 *
 * @Date:2024/8/12 13:18
 * @Author:qs@1.com
 */
public class TestStop {
    private static Object lock = new Object();

    public static void main(String[] args) {
        // 测试 stop 方法
        // testStop();
        // 测试 stop 方法释放对象锁
        testStopWillReleaseObjectLock();
    }

    private static void testStopWillReleaseObjectLock() {
        Thread t = new Thread(() -> {
            System.out.println("-----> t线程等待获取到锁");
            synchronized (lock) {
                System.out.println("t线程获取到锁");
                System.out.println("t线程开始执行");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t线程执行结束");
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println("-----> t2线程等待获取到锁");
            synchronized (lock) {
                System.out.println("t2线程获取到锁");
                System.out.println("t2线程开始执行");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2线程执行结束");
            }
        });

        t.start();

        // 主线程休眠 1 秒
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 停止 t 线程
        t.stop();
        // 启动 t2 线程，能够获取到锁
        t2.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("主线程执行结束");
    }

    private static void testStop() {
        Thread t = new Thread(() -> {
            System.out.println("t线程开始执行");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

        // 停止 t 线程
        t.stop();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("主线程执行结束");
    }
}
