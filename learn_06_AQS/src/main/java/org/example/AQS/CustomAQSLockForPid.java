package org.example.AQS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * ClassName:CustomLock
 * Package:org.example.AQS
 * Description: 自定义 AQS 锁 - 每个商品一把锁
 * 实现同一个商品只能被一个线程操作，不同商品可以被不同线程操作
 *
 * @Date:2024/8/27 13:34
 * @Author:qs@1.com
 */
public class CustomAQSLockForPid {
    private static final ConcurrentHashMap<String, CustomLock> lockMap = new ConcurrentHashMap<>();

    /**
     * 直接用 {@link java.util.concurrent.locks.ReentrantLock} 更加方便
     */
    static class CustomLock {
        private final Sync sync;

        public CustomLock() {
            this.sync = new Sync();
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

    public static CustomLock getLock(String lockName) {
        return lockMap.computeIfAbsent(lockName, k -> new CustomLock());
    }

    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(CustomAQSLockForPid.class);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            new Thread(() -> {
                String lockName = "pid00" + (index % 3 + 1);
                CustomLock lock = CustomAQSLockForPid.getLock(lockName);
                lock.lock();
                try {
                    log.debug(Thread.currentThread().getName() + " - 获取到锁" + lockName);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unLock();
                }
            }, "thread" + i).start();
        }
    }

}
