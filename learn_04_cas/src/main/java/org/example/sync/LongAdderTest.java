package org.example.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * ClassName:LongAdderTest
 * Package:org.example.cas
 * Description: java 高并发之 LongAdder
 * LongAdder 适用于高并发场景，内部维护了一个 Cell 数组，每个 Cell 里面维护了一个 long 类型的值，每个线程会hash取模
 * 访问一个 Cell 进行累加，最后将所有 Cell 的值累加得到最终结果
 *
 * @Date:2024/8/14 15:43
 * @Author:qs@1.com
 */
public class LongAdderTest {
    public static void main(String[] args) {
        testAtomicLongVSLongAdder(10, 10000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(10, 200000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(100, 200000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(1000, 200000);
    }

    private static void testAtomicLongVSLongAdder(final int threadCount, final int times) {
        try {
            long start = System.currentTimeMillis();
            testLongAdder(threadCount, times);
            long cost = System.currentTimeMillis() - start;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>LongAdder方式增加计数" + (threadCount * times) + "次,共计耗时:" + cost);

            long start2 = System.currentTimeMillis();
            testAtomicLong(threadCount, times);
            long cost2 = System.currentTimeMillis() - start2;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>AtomicLong方式增加计数" + (threadCount * times) + "次,共计耗时:" + cost2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testAtomicLong(int threadCount, int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    atomicLong.incrementAndGet();
                }
                countDownLatch.countDown();
            }, "thread-" + i).start();
        }
        countDownLatch.await();
    }

    private static void testLongAdder(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    longAdder.add(1);
                }
                countDownLatch.countDown();
            }, "thread-" + i).start();
        }
        countDownLatch.await();
    }

}
