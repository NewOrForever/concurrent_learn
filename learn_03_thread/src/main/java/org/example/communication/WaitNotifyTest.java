package org.example.communication;

/**
 * ClassName:WaitNotifyTest
 * Package:org.example.common_method.communication
 * Description: java 线程间通信之 wait/notify 等待唤醒机制
 * 1. wait 方法会让当前线程从 Running 状态进入 Waiting 状态，会释放对象锁
 * 2. notify 方法会唤醒一个正在等待的线程，notifyAll 方法会唤醒所有正在等待的线程
 * 3. wait/notify 方法必须在同步代码块中使用，否则会抛出 IllegalMonitorStateException 异常
 * 4. wait/notify 方法必须在同一个对象锁上使用，否则会抛出 IllegalMonitorStateException 异常
 * 5. wait 方法会释放对象锁，notify 方法不会释放对象锁
 *
 * @Date:2024/8/12 15:29
 * @Author:qs@1.com
 */
public class WaitNotifyTest {
    private static final Object lock = new Object();
    private static boolean flag = true;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println("wait start .......");
                        /**
                         * wait 方法会让当前线程从 Running 状态进入 Waiting 状态，会释放对象锁
                         * 执行 wait 方法后该线程会让出 CPU 的执行权（开始执行下面的 notify thread），等待被唤醒
                         */
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("wait end .......");
            }
        }).start();

        new Thread(() -> {
            System.out.println("notify thread 开始执行");
            if (flag) {
                synchronized (lock) {
                    if (flag) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        lock.notify();
                        System.out.println("notify .......");
                        flag = false;
                    }
                }
            }
        }).start();
    }

}
