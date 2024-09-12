package org.example.AQS.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName:ReentrantReadWriteLockDemo
 * Package:org.example.AQS.readwritelock
 * Description: ReentrantReadWriteLock 读写锁示例
 * ReentrantReadWriteLock 读写锁是一种高级的同步机制，它允许多个线程同时读共享资源，但是在写共享资源时，所有的读写线程都会被阻塞
 * 读写锁的实现原理：读锁和写锁是分离的，读锁是共享锁，写锁是排他锁
 *
 * @Date:2024/9/11 15:25
 * @Author:qs@1.com
 */
public class ReentrantReadWriteLockDemo {
    // 创建一个读写锁
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    public static void main(String[] args) {

        // 创建一个读线程
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                get("key001");
            }, "readThread").start();
        }

        // 创建一个写线程
        new Thread(() -> {
            put("key001", "value001");
        }, "writeThread").start();
    }

    private static void put(String key, String value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " - 写入" + key + "数据为：" + value);
            // 模拟写数据
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private static void get(String key) {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " - 读取" + key + "数据");
            // 模拟读取数据
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

}
