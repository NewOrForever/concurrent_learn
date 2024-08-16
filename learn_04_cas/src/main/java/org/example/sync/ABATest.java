package org.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;

/**
 * ClassName:ABATest
 * Package:org.example.cas
 * Description: CAS 的ABA 问题
 * ABA 问题是指在 CAS 算法中，由于 CAS 需要在操作值的时候检查值有没有发生变化，如果没有发生变化则更新，但是如果
 * 一个值原来是 A，后来变成了 B，然后又变成了 A，那么使用 CAS 进行检查时会发现它的值没有发生变化，但是实际上却变化了，这就是 CAS 的ABA问题。
 * 为了解决这个问题，Java 的并发包中提供了一个带有标记的原子引用类 AtomicStampedReference，它可以通过控制变量值的版本来保证CAS的正确性。
 * AtomicStampedReference 维护了一个对象引用和一个整数标记，在每次 CAS 操作时，除了检查对象引用的值之外，还会检查整数标记的值。
 * 当需要更新变量时，对象引用和整数标记都需要被同时更新，如果对象引用的值与预期值相同，且整数标记的值与预期值相同，那么就更新对象引用的值和整数标记的值。
 * 通过 AtomicStampedReference 可以解决 CAS 的ABA问题。
 *
 * @Date:2024/8/13 14:59
 * @Author:qs@1.com
 */
@Slf4j
public class ABATest {
    /**
     * 模拟 ABA 问题
     */
    public static void main(String[] args) {
        // 模拟 ABA 问题
        // mockABAProblem();
        // 解决 ABA 问题
        solveABAProblemWithAtomicStamp();
    }

    private static void solveABAProblemWithAtomicStamp() {
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 1);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int[] stampHolder = new int[1];
                int value = atomicStampedReference.get(stampHolder);
                int stamp = stampHolder[0];
                log.debug("thread1 read value: " + value + ", stamp: " + stamp);

                // 阻塞 1 秒，模拟其他线程对 value 进行了修改
                LockSupport.parkNanos(1000000000L);

                // CAS 将 value 从 1 改为 3
                if (atomicStampedReference.compareAndSet(value, 3, stamp, stamp + 1)) {
                    log.debug("thread1 update value from " + value + " to 3");
                } else {
                    log.debug("thread1 update failed");
                }
            }
        }, "thread1");
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int[] stampHolder = new int[1];
                int value = atomicStampedReference.get(stampHolder);
                int stamp = stampHolder[0];
                log.debug("thread2 read value: " + value + ", stamp: " + stamp);

                // CAS 将 value 从 1 改为 2
                if (atomicStampedReference.compareAndSet(value, 2, stamp, stamp + 1)) {
                    log.debug("thread2 update value from " + value + " to 2");

                    // CAS 将 value 从 2 改为 1
                    value = atomicStampedReference.get(stampHolder);
                    stamp = stampHolder[0];
                    log.debug("thread2 read value: " + value + ", stamp: " + stamp);
                    if (atomicStampedReference.compareAndSet(value, 1, stamp, stamp + 1)) {
                        log.debug("thread2 update value from " + value + " to 1");
                    }
                }
            }
        }, "thread2");
        thread2.start();
    }

    private static void mockABAProblem() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int value = atomicInteger.get();
                log.debug("thread1 read value: " + value);

                // 阻塞 1 秒，模拟其他线程对 value 进行了修改
                LockSupport.parkNanos(1000000000L);

                // CAS 将 value 从 1 改为 3
                if (atomicInteger.compareAndSet(value, 3)) {
                    log.debug("thread1 update value from " + value + " to 3");
                } else {
                    log.debug("thread1 update failed");
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int value = atomicInteger.get();
                log.debug("thread2 read value: " + value);

                // CAS 将 value 从 1 改为 2
                if (atomicInteger.compareAndSet(value, 2)) {
                    log.debug("thread2 update value from " + value + " to 2");

                    // CAS 将 value 从 2 改为 1
                    value = atomicInteger.get();
                    log.debug("thread2 read value: " + value);
                    if (atomicInteger.compareAndSet(value, 1)) {
                        log.debug("thread2 update value from " + value + " to 1");
                    }
                }
            }
        });
        thread2.start();
    }

}
