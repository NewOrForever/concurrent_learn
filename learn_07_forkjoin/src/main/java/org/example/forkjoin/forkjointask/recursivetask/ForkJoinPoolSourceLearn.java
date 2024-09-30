package org.example.forkjoin.forkjointask.recursivetask;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * ClassName:ForkJoinPoolSourceLearn
 * Package:org.example.forkjoin.forkjointask.recursivetask
 * Description:
 *
 * @Date:2024/9/27 16:06
 * @Author:qs@1.com
 */
public class ForkJoinPoolSourceLearn {
    public static void main(String[] args) {
        Unsafe unsafe = getUnsafe();
        Class<?> ak = ForkJoinTask[].class;
        int scale = unsafe.arrayIndexScale(ak);
        if ((scale & (scale - 1)) != 0)
            throw new Error("data type scale not a power of two");
        System.out.println(scale);
        int ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        System.out.println(ASHIFT);
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
