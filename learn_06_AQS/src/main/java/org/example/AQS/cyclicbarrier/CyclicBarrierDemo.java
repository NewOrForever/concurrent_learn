package org.example.AQS.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:cyclicbarrier
 * Package:org.example.AQS
 * Description: CyclicBarrier 示例
 * CyclicBarrier 可以实现让一组线程等待至某个状态（屏障点）之后再全部同时执行
 * 实现原理：
 * 1. CyclicBarrier 内部有一个计数器，每个线程调用 await() 方法时，计数器减 1
 * 2. 当计数器的值为 0 时，表示所有线程都已经调用了 await() 方法，可以继续执行
 * 3. CyclicBarrier 的计数器可以重复使用，当计数器的值减到 0 之后，会重置为初始值
 * 4. CyclicBarrier 是线程安全的
 * 5. CyclicBarrier 适用于一组线程互相等待的场景
 *
 * @Date:2024/9/9 16:05
 * @Author:qs@1.com
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        // 创建一个 CyclicBarrier，初始值为 3
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        // 创建三个线程
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " - 执行任务");
                try {
                    // 模拟线程执行任务
                    Thread.sleep(1000);
                    // 等待其他线程
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + " - 执行完毕");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread" + i).start();
        }
    }

}
