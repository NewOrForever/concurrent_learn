package org.example.AQS.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:ReentrantLockDemo
 * Package:org.example.AQS
 * Description: 测试 ReentrantLock 的功能
 * 1. 同步锁
 * 2. 可重入锁：当前线程尝试获取锁时，如果已经获取到锁，可以再次获取，而不会被阻塞
 * 3. 公平锁
 * 4. 锁超时
 * 5. 锁中断
 * 6. 锁绑定多个条件
 *
 * @Date:2024/8/26 15:13
 * @Author:qs@1.com
 */
@Slf4j
public class ReentrantLockTest {
    private static int sum = 0;
    private static Lock lock = new ReentrantLock();
    private static Condition cigCon = lock.newCondition();
    private static Condition takeCon = lock.newCondition();
    private static boolean hashcig = false;
    private static boolean hastakeout = false;


    public static void main(String[] args) throws InterruptedException {
        // 测试同步锁
        // testSync();

        // 测试可重入锁
        // testReentrant();

        // 测试锁中断
        // testInterrupt();

        // 测试锁超时 - 立即失败
        // testTryLockImmediateFail();
        // 测试锁超时 - 超时失败
        // testTryLockTimeoutFail();

        // 测试公平锁
        // testFairLock();

        // 测试 Condition 条件变量
        testCondition();
    }

    private static void testCondition() throws InterruptedException {
        new Thread(() -> {
            cigratee();
        }, "有烟才干活线程").start();

        new Thread(() -> {
            takeout();
        }, "有饭才干活线程").start();

        Thread.sleep(2000);

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("main线程已经送来烟");
                hashcig = true;
                // 唤醒有烟才干活线程
                cigCon.signal();
            } finally {
                lock.unlock();
            }
        }, "送烟").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("main线程已经送来饭");
                hastakeout = true;
                // 唤醒有饭才干活线程
                takeCon.signal();
            } finally {
                lock.unlock();
            }
        }, "送外卖").start();
    }

    // 送烟
    public static void cigratee() {
        lock.lock();
        try {
            while (!hashcig) {
                try {
                    log.debug("没有烟，歇一会");
                    cigCon.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.debug("有烟了，干活");
        } finally {
            lock.unlock();
        }
    }

    // 送外卖
    public static void takeout() {
        lock.lock();
        try {
            while (!hastakeout) {
                try {
                    log.debug("没有饭，歇一会");
                    takeCon.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.debug("有饭了，干活");
        } finally {
            lock.unlock();
        }
    }


    private static void testFairLock() throws InterruptedException {
        /**
         * 默认是非公平锁，构造函数传入true则是公平锁
         * - 公平锁：强行插入的线程需要等待 t 线程执行完毕后才能获取锁
         *      - 因为公平锁时按照线程加入同步等待队列的顺序获取锁
         * - 非公平锁：强行插入的线程可以直接获取锁
         *      - 因为非公平锁时，新进来的线程会先去尝试获取锁，如果获取失败，再加入同步等待队列
         */
        ReentrantLock fairLock = new ReentrantLock(true);
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                fairLock.lock();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug(Thread.currentThread().getName() + "获取到了锁 >>> running ...");
                } finally {
                    fairLock.unlock();
                }
            }, "t" + i).start();
        }

        Thread.sleep(1000);

        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                fairLock.lock();
                try {
                    log.debug(Thread.currentThread().getName() + "获取到了锁 >>> running ...");
                } finally {
                    fairLock.unlock();
                }
            }, "强行插入" + i).start();
        }
    }

    private static void testTryLockTimeoutFail() {
        Thread t1 = new Thread(() -> {
            log.debug("t1启动。。。");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("t1 获取锁失败后，等待2秒后获取锁失败 >>> 返回false");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            try {
                log.debug("t1获取到了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        try {
            log.debug("main线程获取到了锁");
            t1.start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            log.debug("main线程释放锁");
            lock.unlock();
        }
    }

    private static void testTryLockImmediateFail() {
        Thread t1 = new Thread(() -> {
            log.debug("t1启动。。。");
            // 注意： 即使是设置的公平锁，此方法也会立即返回获取锁成功或失败，公平策略不生效
            if (!lock.tryLock()) {
                log.debug("t1获取锁失败 >>> 立即返回false");
                return;
            }
            try {
                log.debug("t1获取到了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        try {
            log.debug("main线程获取到了锁");
            t1.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            log.debug("main线程释放锁");
            lock.unlock();
        }
    }

    private static void testInterrupt() {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            log.debug("t1启动。。。");
            try {
                lock.lockInterruptibly();
                try {
                    log.debug("t1获取到了锁");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("t1等待获取锁时被中断");
            }

        }, "t1");

        lock.lock();
        try {
            log.debug("main线程获取到了锁");
            // 先让t1执行
            t1.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            t1.interrupt();
            /**
             * log.debug("线程t1执行中断, t1 中断状态:{}", t1.isInterrupted());
             * - 如果这里打印时调用了 t1.isInterrupted()，先执行t1的中断异常处理，再执行main线程的finally块
             * - 如果没有调用 t1.isInterrupted()，则main线程的finally块先执行，再执行t1的中断异常处理
             * 总结：调用t1.isInterrupted()会触发线程调度器重新评估线程状态，导致t1的中断异常处理先于主线程的finally块执行
             */
            log.debug("线程t1执行中断");
        } finally {
            log.debug("main线程释放锁");
            lock.unlock();
        }
    }

    private static void testReentrant() {
        lock.lock();
        try {
            log.debug("执行testReentrant");
            method1();
        } finally {
            lock.unlock();
        }
    }

    private static void method1() {
        lock.lock();
        try {
            log.debug("执行method1");
            method2();
        } finally {
            lock.unlock();
        }
    }

    private static void method2() {
        lock.lock();
        try {
            log.debug("执行method2");
        } finally {
            lock.unlock();
        }
    }

    private static void testSync() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    for (int j = 0; j < 10000; j++) {
                        sum++;
                    }
                } finally {
                    lock.unlock();
                }
            }).start();
        }

        Thread.sleep(2000);
        System.out.println(sum);
    }

}
