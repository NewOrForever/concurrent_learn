package org.example.AQS.share.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * ClassName:CountDownLatchMultiThreadAwait
 * Package:org.example.AQS.condition.countdownlatch
 * Description: CountDownLatch 应用场景1：多线程等待
 * 让多个线程等待某个事件的发生，然后同时执行
 *
 * @Date:2024/9/9 10:23
 * @Author:qs@1.com
 */
public class CountDownLatchMultiThreadAwait {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个计数器，初始值为 1
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 创建5个线程
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                // 等待计数器的值为 0
                try {
                    countDownLatch.await();
                    System.out.println(Thread.currentThread().getName() + " - 开始执行任务");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "thread" + i).start();
        }

        System.out.println("裁判准备发令");
        // 裁判准备发令
        Thread.sleep(2000);
        // 发令枪：执行发令
        countDownLatch.countDown();
    }

}
