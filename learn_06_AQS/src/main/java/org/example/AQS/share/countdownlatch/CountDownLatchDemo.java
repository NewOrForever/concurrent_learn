package org.example.AQS.share.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * ClassName:CountDownLatchDemo
 * Package:org.example.AQS.condition
 * Description: CountDownLatch 示例
 * CountDownLatch 是一个同步工具类，用来协调多个线程之间的同步，通过AQS 的共享模式实现
 * CountDownLatch 有一个计数器，初始化时指定一个数值，当计数器的值为 0 时，表示所有线程已经准备就绪，可以继续执行
 * CountDownLatch 有两个方法：countDown() 和 await()
 * countDown()：计数器减 1
 * await()：等待计数器的值为 0
 * CountDownLatch 适用于一个线程等待多个线程的场景
 * CountDownLatch 是一次性的，计数器的值减到 0 之后，就不能再重置为初始值
 * CountDownLatch 是线程安全的
 * CountDownLatch 的计数器不能重复使用，如果需要重复使用，可以考虑使用 CyclicBarrier
 *
 * @Date:2024/9/9 10:08
 * @Author:qs@1.com
 */
@Slf4j
public class CountDownLatchDemo {
    public static void main(String[] args) {
        // 创建一个计数器，初始值为 3
        CountDownLatch countDownLatch = new CountDownLatch(3);
        // 创建三个线程
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                log.debug(Thread.currentThread().getName() + " - 执行任务");
                try {
                    // 模拟线程执行任务
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 计数器减 1
                countDownLatch.countDown();
            }, "thread" + i).start();
        }
        try {
            // 等待计数器的值为 0
            countDownLatch.await();
            log.debug("所有线程执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
