package org.example.AQS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * ClassName:CustomLock
 * Package:org.example.AQS
 * Description: 自定义 AQS 锁
 *
 * @Date:2024/8/27 13:34
 * @Author:qs@1.com
 */
public class CustomAQSLock {
    private final Sync sync;
    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, arg)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new RuntimeException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        public void lock() {
            acquire(1);
        }
    }

    public CustomAQSLock() {
        this.sync = new Sync();
    }

    public static void main(String[] args) {
        CustomAQSLock lock = new CustomAQSLock();
        Logger log = LoggerFactory.getLogger(CustomAQSLock.class);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    log.debug(Thread.currentThread().getName() + " - 获取到锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unLock();
                }
            }, "thread" + i).start();
        }
    }

    public void lock() {
        sync.lock();
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void unLock() {
        sync.release(1);
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

}
