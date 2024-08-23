package org.example.sync.biaslock;

import org.openjdk.jol.info.ClassLayout;

/**
 * ClassName:TestBiasLockRevoke
 * Package:org.example.sync.biaslock
 * Description: 测试偏向锁撤销为无锁状态
 *
 * @Date:2024/8/22 10:29
 * @Author:qs@1.com
 */
public class TestBiasLockRevokeToNoLock {
    static class TestObject {
        // 空类用于测试锁
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(4000);
        // 使主线程持有偏向锁
        TestObject testObject = new TestObject();

        // 主线程持有锁
        synchronized (testObject) {
            System.out.println("Main thread acquired biased lock");
            printObjectMarkWord(testObject);
        }

        // 模拟GC后，检查是否有竞争
        System.gc();
        System.out.println("After GC, checking lock state:");
        printObjectMarkWord(testObject);

        // 短暂停滞以模拟GC后偏向锁可能的撤销行为
        Thread.sleep(100);
        System.out.println("After short delay, checking lock state:");
        printObjectMarkWord(testObject);

        // 主线程再次尝试获取锁
        synchronized (testObject) {
            System.out.println("Main thread re-acquired lock after GC");
            printObjectMarkWord(testObject);
        }
        System.out.println("After main thread released lock:");
        printObjectMarkWord(testObject);

        // 创建一个新线程
        Thread t1 = new Thread(() -> {
            synchronized (testObject) {
                System.out.println("New thread acquired lock");
                printObjectMarkWord(testObject);
            }
        });

        // 启动新线程并等待其完成
        t1.start();
        t1.join();

        // 再次检查对象的锁状态
        System.out.println("After thread t1, checking lock state:");
        printObjectMarkWord(testObject);
    }

    // 打印对象的Mark Word以查看锁状态
    private static void printObjectMarkWord(TestObject obj) {
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
