package org.example.cas;

import org.example.UnsafeFactory;
import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:AtomicIntegerTest
 * Package:org.example.cas
 * Description:
 *
 * @Date:2024/8/13 16:40
 * @Author:qs@1.com
 */
public class AtomicIntegerTest {
    private static AtomicInteger sum = new AtomicInteger(0);

    public static void main(String[] args) {
        // AtomicInteger 的一些方法测试
        // testAoticIntegerSomeMethod();
        // AtomicInteger 的自旋 CAS 操作测试
        // testCASSpin();
        // 测试原子自增
        testAtomicSelfIncrement();
    }

    private static void testAtomicSelfIncrement() {
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 原子自增  CAS
                    sum.incrementAndGet();
                }
            });
            thread.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sum.get());
    }

    private static void testCASSpin() {
        Unsafe unsafe = UnsafeFactory.getUnsafe();
        int val = 0;
        Entity entity = new Entity();
        entity.x = 10;
        long offset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
        do {
            val++;
            System.out.println("val: " + val);
        } while(!unsafe.compareAndSwapInt(entity, offset, val, 20));

        System.out.println("final return value: " + val);
        System.out.println("final entity.x: " + entity.x);
    }

    private static void testAoticIntegerSomeMethod() {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println("-------->" + atomicInteger.get());
        System.out.println(atomicInteger.getAndIncrement());
        System.out.println("-------->" + atomicInteger.get());
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println("-------->" + atomicInteger.get());
        System.out.println(atomicInteger.getAndAdd(5));
        System.out.println("-------->" + atomicInteger.get());
        System.out.println(atomicInteger.addAndGet(5));
        System.out.println("-------->" + atomicInteger.get());
        System.out.println(atomicInteger.getAndSet(10));
        System.out.println("-------->" + atomicInteger.get());
        atomicInteger.set(100);
        System.out.println("-------->" + atomicInteger.get());
    }
}
