package org.example.AQS.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * ClassName:SemaphoreTest
 * Package:org.example.AQS.semaphore
 * Description: Semaphore 信号量测试demo
 * 信号量是一个计数器，用来保护一个或多个共享资源的访问
 * 信号量的值为 n 时，表示有 n 个可用的资源
 * 当一个线程请求资源时，会将信号量的值减 1，当信号量的值为 0 时，表示没有可用的资源，此时线程会被阻塞，直到有线程释放资源，将信号量的值加 1
 * Semaphore 有两种模式：公平模式和非公平模式
 *
 * @Date:2024/8/31 14:10
 * @Author:qs@1.com
 */
@Slf4j
public class SemaphoreSimpleTest {
    public static void main(String[] args) {
        /**
         * 创建一个信号量，初始值为 3（资源数为3，即最多有3个线程可以同时访问共享资源）
          */
        Semaphore semaphore = new Semaphore(3);
        // 创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // 获取一个许可
                    semaphore.acquire();
                    log.debug(Thread.currentThread().getName() + " - 获取到许可");
                    // 模拟线程执行任务
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放一个许可
                    log.debug(Thread.currentThread().getName() + " - 释放许可");
                    semaphore.release();
                }
            }, "thread" + i).start();
        }
    }

}
