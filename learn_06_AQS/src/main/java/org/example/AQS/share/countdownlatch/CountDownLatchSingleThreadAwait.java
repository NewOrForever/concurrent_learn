package org.example.AQS.share.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ClassName:CountDownLatchSingleThreadAwait
 * Package:org.example.AQS.condition.countdownlatch
 * Description: CountDownLatch 应用场景2：单线程等待
 * 让一个线程等待多个线程完成后再进行汇总合并
 *
 * @Date:2024/9/9 10:30
 * @Author:qs@1.com
 */
public class CountDownLatchSingleThreadAwait {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000 + ThreadLocalRandom.current().nextInt(1000));
                    System.out.println(Thread.currentThread().getName() + " - finish task");

                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "thread" + i).start();
        }

        // 主线程在阻塞，当计数器为 0，就唤醒主线程往下执行
        countDownLatch.await();
        System.out.println("主线程：所有任务执行完毕，进行结果汇总");
    }

}
