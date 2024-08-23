package org.example.sync.lockupgrade;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * ClassName:Test
 * Package:org.example.sync.lockupgrade
 * Description:
 *
 * @Date:2024/8/22 9:17
 * @Author:qs@1.com
 */
@Slf4j
public class Test {
    private static class SimpleObject {
        // 一个普通的对象
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(4000);
        Object obj = new Object();
        log.debug(ClassLayout.parseInstance(obj).toPrintable());

        // 第一个线程获取偏向锁
        Thread t1 = new Thread(() -> {
            synchronized (obj) {
                log.debug(Thread.currentThread().getName() + " obtained the lock ");
                // 此时 obj 已进入 "已偏向已锁定" 状态
                log.debug(ClassLayout.parseInstance(obj).toPrintable());
            }
            log.debug(Thread.currentThread().getName() + " released the lock \n" + ClassLayout.parseInstance(obj).toPrintable());
        }, "Thread-1");
        t1.start();
        /**
         * 加上 join 后立即start t2，t2 中打印的对象锁依然为偏向锁且偏向线程和 t1 中打印的偏向线程一致
         *  - 如果 join 后再 sleep 一下，t2 中获取锁后，打印的对象锁状态才会是轻量级锁（正常来说就对象锁已偏向未锁定时另一个线程获取锁就会升级为轻量级锁）
          */
        /*t1.join();
        Thread.sleep(10);*/

        // 第二个线程尝试获取锁
        Thread t2 = new Thread(() -> {
            log.debug(Thread.currentThread().getName() + " is trying to obtain the lock");
            log.debug(ClassLayout.parseInstance(obj).toPrintable());
            synchronized (obj) {
                log.debug(Thread.currentThread().getName() + " obtained the lock");
                // 如果发生了锁竞争，偏向锁可能升级为轻量级锁或重量级锁
                log.debug(ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "Thread-2");
        t2.start();


        Thread.sleep(5000);
        log.debug("After Thread-2 released the lock");
        log.debug(ClassLayout.parseInstance(obj).toPrintable());

    }
}
