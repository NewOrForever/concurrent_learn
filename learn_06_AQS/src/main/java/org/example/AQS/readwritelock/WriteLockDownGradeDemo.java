package org.example.AQS.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName:WriteLockDownGrade
 * Package:org.example.AQS.readwritelock
 * Description: 读写锁 - 写锁降级为读锁示例
 *
 * @Date:2024/9/11 17:20
 * @Author:qs@1.com
 */
public class WriteLockDownGradeDemo {
    // 创建一个读写锁
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();
    private volatile boolean update = false;

    public static void main(String[] args) {
        WriteLockDownGradeDemo downGrade = new WriteLockDownGradeDemo();
        downGrade.processData();
    }

    public void processData() {
        readLock.lock();
        if (!update) {
            // 必须先释放读锁
            readLock.unlock();
            /**
             * 锁降级的范式是从这里获取到写锁开始的
             * >>>>>>>>>> 锁降级从获取到写锁开始
              */
            writeLock.lock();
            try {
                if (!update) {
                    // TODO 准备数据的流程（略）
                    update = true;
                }
                readLock.lock();
            } finally {
                writeLock.unlock();
            }
            /**
             * 释放写锁前先获取读锁，这样就可以保证数据的可见性，避免在释放写锁后被其他获取到写锁的线程修改数据
             * <<<<<<<<<< 锁降级完成，写锁降级为读锁
              */
        }
        try {
            //TODO  使用数据的流程（略）
        } finally {
            readLock.unlock();
        }
    }

}
