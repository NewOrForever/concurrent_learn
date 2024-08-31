package org.example.AQS.reentrantlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:ReentrantLockSourceTest
 * Package:org.example.AQS.reentrantlock
 * Description: ReentrantLock 源码解析
 *
 * @Date:2024/8/27 14:36
 * @Author:qs@1.com
 */
public class ReentrantLockSourceTest {
    public static void main(String[] args) {
        // ReentrantLock 源码解析
        ReentrantLock lock = new ReentrantLock();
        Logger log = LoggerFactory.getLogger(ReentrantLockSourceTest.class);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    log.debug(Thread.currentThread().getName() + " - 获取到锁");
                } finally {
                    lock.unlock();
                }
            }, "thread" + i).start();
        }
    }

}
