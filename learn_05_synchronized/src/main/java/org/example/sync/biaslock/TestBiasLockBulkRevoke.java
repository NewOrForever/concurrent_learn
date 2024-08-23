package org.example.sync.biaslock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * ClassName:TestBiasLockBulkRevoke
 * Package:org.example.sync.biaslock
 * Description: 测试偏向锁批量撤销
 *
 * @Date:2024/8/22 16:32
 * @Author:qs@1.com
 */
@Slf4j
public class TestBiasLockBulkRevoke {

    public static void main(String[] args) throws InterruptedException {
        // 延时产生可偏向对象
        Thread.sleep(4000);
        // 创建一个list，来存放锁对象
        List<Object> list = new ArrayList<>();

        // 线程1
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                // 新建锁对象
                Object lock = new Object();
                synchronized (lock) {
                    list.add(lock);
                }
            }
            try {
                // 为了防止JVM线程复用，在创建完对象后，保持线程thead1状态为存活
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thead1").start();

        // 睡眠3s钟保证线程thead1创建对象完成
        Thread.sleep(3000);
        log.debug("打印thead1，list中第20个对象的对象头：");
        log.debug((ClassLayout.parseInstance(list.get(19)).toPrintable()));

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                Object obj = list.get(i);
                synchronized (obj) {
                    if (i >= 15 && i <= 21 || i >= 38) {
                        log.debug("thread2-第" + (i + 1) + "次加锁执行中\t" +
                                ClassLayout.parseInstance(obj).toPrintable());
                    }
                }
                if (i == 17 || i == 19) {
                    log.debug("thread2-第" + (i + 1) + "次释放锁\t" +
                            ClassLayout.parseInstance(obj).toPrintable());
                }
            }
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thead2").start();


        Thread.sleep(3000);

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                Object lock = list.get(i);
                if (i >= 17 && i <= 21 || i >= 35 && i <= 41) {
                    log.debug("thread3-第" + (i + 1) + "次准备加锁\t" +
                            ClassLayout.parseInstance(lock).toPrintable());
                }
                synchronized (lock) {
                    if (i >= 17 && i <= 21 || i >= 35 && i <= 41) {
                        log.debug("thread3-第" + (i + 1) + "次加锁执行中\t" +
                                ClassLayout.parseInstance(lock).toPrintable());
                    }
                }
            }
        }, "thread3").start();

        Thread.sleep(3000);
        log.debug("查看新创建的对象");
        log.debug((ClassLayout.parseInstance(new Object()).toPrintable()));

        LockSupport.park();
    }
}
