package org.example;

import sun.misc.Unsafe;

/**
 * ClassName:CASLock
 * Package:org.example
 * Description: CAS 实现自旋锁
 *
 * @Date:2024/8/13 14:32
 * @Author:qs@1.com
 */
public class CASLock {
    //加锁标记
    private volatile int state;
    private static final Unsafe UNSAFE;
    private static final long OFFSET;

    static {
        try {
            UNSAFE = UnsafeFactory.getUnsafe();
            OFFSET = UnsafeFactory.getFieldOffset(UNSAFE, CASLock.class, "state");
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public boolean cas() {
        return UNSAFE.compareAndSwapInt(this, OFFSET, 0, 1);
    }

    public void lock() {
        while (!cas()) {

        }
    }

    public void unlock() {
        state = 0;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
