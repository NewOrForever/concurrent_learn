package org.example.cas;

import org.example.UnsafeFactory;
import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * ClassName:AtomicIntegerTest
 * Package:org.example.cas
 * Description:
 *
 * @Date:2024/8/13 16:40
 * @Author:qs@1.com
 */
public class AtomicIntegerArrayTest {

    public static void main(String[] args) {
        int[] valArr = new int[]{1, 2, 3, 4, 5};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(valArr);
        System.out.println("-------->" + atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.getAndIncrement(0));
        System.out.println("-------->" + atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.incrementAndGet(0));
        System.out.println("-------->" + atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.getAndAdd(0, 5));
        System.out.println("-------->" + atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.addAndGet(0, 5));
        System.out.println("-------->" + atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.getAndSet(0, 10));
        System.out.println("-------->" + atomicIntegerArray.get(0));
        atomicIntegerArray.set(0, 20);
        System.out.println("-------->" + atomicIntegerArray.get(0));
    }

}
