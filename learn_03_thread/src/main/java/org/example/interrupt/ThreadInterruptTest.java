package org.example.interrupt;

import java.util.TreeMap;

/**
 * ClassName:ThreadInterruptTest
 * Package:org.example.interrupt
 * Description: 线程中断测试
 * 1. 线程中断是一种线程间的协作机制，通过中断可以让线程停止正在执行的任务
 * 2. 线程中断是通过设置中断标记来实现的，通过调用 interrupt 方法可以设置中断标记
 * 3. 线程中断不会立即停止线程，而是设置中断标记，线程可以通过检查中断标记来判断是否需要停止执行任务
 * 4. 线程可以通过 isInterrupted 方法检查中断标记，通过 interrupted 方法检查中断标记并清除中断标记
 * 5. 线程可以通过抛出 InterruptedException 异常来响应中断，抛出异常会清除中断标记
 *
 * @Date:2024/8/12 14:20
 * @Author:qs@1.com
 */
public class ThreadInterruptTest {
    static int i = 0;

    public static void main(String[] args) throws InterruptedException {
        // 测试线程中断机制
        // testInterrupt();
        // 测试利用中断机制优雅的停止线程
        // testStopThreadGracefully();
        // 测试线程中有 sleep 能否感知到中断
        testSleepInterrupt();
    }

    private static void testSleepInterrupt() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (!Thread.currentThread().isInterrupted() && count < 1000) {
                    System.out.println("count = " + count++);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // 感知到中断，清除中断标记
                        System.out.println("线程被中断：interrupted，当前线程中断标记：" + Thread.currentThread().isInterrupted());
                        /**
                         * 重新设置中断标记
                         * 如果不重新设置中断标记，那么 !Thread.currentThread().isInterrupted() 该判断条件
                         * 就是 true，线程会继续执行，只有在不满足 count < 1000 的条件时才会退出循环，这样就会导致线程无法正确停止
                         */
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("线程停止： stop thread");
            }
        });

        thread.start();
        Thread.sleep(5);
        // 中断线程（将中断标记设置为 true）
        thread.interrupt();
    }

    private static void testStopThreadGracefully() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                // 检查中断标记（不会清除中断标记）
                // 中断后线程中断标记会被设置为 true，所以while循环会退出
                // 通过使用 Thread.currentThread().isInterrupted() 方法来通过检查中断标记来判断是否需要停止线程
                while (!Thread.currentThread().isInterrupted() && count < 1000) {
                    System.out.println("count = " + count++);
                }
                System.out.println("线程停止： stop thread");
            }
        });

        thread.start();
        Thread.sleep(3);
        // 中断线程（将中断标记设置为 true）
        thread.interrupt();
    }

    private static void testInterrupt() {
        System.out.println("begin");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    i++;
                    System.out.println(i);
                    /**
                     * Thread.interrupted()  清除中断标志位
                     * Thread.currentThread().isInterrupted() 不会清除中断标志位
                     * 总之最后都是调用了 {@link Thread#isInterrupted(boolean)} 这个 jvm 原生方法
                     * 参数 boolean ClearInterrupted 说明：
                     *  - 传入的参数为 true 时，会清除中断标记
                     *  - 传入的参数为 false 时，不会清除中断标记
                     */
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("=========");
                    }
                    if (i == 10) {
                        break;
                    }

                }
            }
        });

        t1.start();
        //不会停止线程t1,只会设置一个中断标志位 flag=true
        t1.interrupt();
    }
}