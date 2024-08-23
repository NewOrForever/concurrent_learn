package org.example.sync.lockupgrade;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * ClassName:TestLockUpgrade
 * Package:org.example.sync.lockupgrade
 * Description: 测试锁升级
 *
 * @Date:2024/8/21 8:49
 * @Author:qs@1.com
 */
@Slf4j
public class TestLockUpgrade {
    public static void main(String[] args) throws InterruptedException {
        log.debug(ClassLayout.parseInstance(new Object()).toPrintable());
        // HotSpot 虚拟机在启动后有个 4s 的延迟才会对每个新建的对象开启偏向锁模式
        Thread.sleep(4000);
        Object obj = new Object();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());
                synchronized (obj) {
                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());
            }

        }, "thread1");
        thread1.start();

        /**
         * 控制线程竞争时机，保证 thread2 在尝试获取锁时，thread1 还没有释放锁（对象锁状态为已偏向已锁定）
         * 这里是为了模拟线程轻微竞争（线程交替执行同步代码块）的场景
         *  - 线程1先获取到锁，然后释放锁，紧接着线程2获取到锁，这样就能模拟出偏向锁升级为轻量级锁的场景
         *  - sleep(1) 能模拟出来的话就用 sleep(1)，不能模拟出来就用 sleep(2)，再模拟不出来就再加大点 sleep 时间
         *  - sleep(1) 后：对象锁状态为已偏向已锁定 -> thread2尝试获取锁 -> 失败则进行一次 CAS -> 成功（说明thread1 此时已释放锁）则获取锁且对象锁状态升级为轻量级锁
         *
         * 注释调这行代码
         *  - 多线程竞争锁的场景，这样就能模拟出轻量级锁膨胀为重量级锁的场景
          */
        /**
         * 模拟偏向锁撤销：锁对象已偏向已锁定 -> 锁对象升级为轻量级锁
         * 这里如果 sleep 时间太长（比如 sleep(3000)）导致 thread1 都已经终止了，thread2 才start，那么 JVM 可能会复用 thread1 的内核线程给 thread2
         * - 这样 thread2 就不会再去竞争 thread1 的偏向锁了，而是直接获取到偏向锁
          */
        Thread.sleep(2);
        /*log.debug("主线程休眠后 \n" + ClassLayout.parseInstance(obj).toPrintable());*/

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());
                synchronized (obj) {
                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "thread2");
        thread2.start();

        Thread.sleep(5000);
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
    }

}
