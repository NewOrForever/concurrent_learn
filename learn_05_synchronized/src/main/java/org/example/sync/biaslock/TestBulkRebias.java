package org.example.sync.biaslock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * ClassName:TestBatchRebias
 * Package:org.example.sync.biaslock
 * Description: 测试批量重偏向
 *
 * @Date:2024/8/21 14:48
 * @Author:qs@1.com
 */
@Slf4j
public class TestBulkRebias {
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
                // 加锁后偏向锁状态为已偏向已锁定
                // 释放锁后偏向锁状态为已偏向未锁定
                synchronized (lock) {
                    list.add(lock);
                }
            }
            try {
                /**
                 * 为了防止JVM线程复用，在创建完对象后，保持线程thread1状态为存活
                 *  - 这样就不会出现线程thread1终止后，线程thread2 start 复用线程thread1的内核线程
                 *  - 因为下面 sleep 了 3s，所以这里需要 sleep 下，保证线程thread1存活，避免 thread1 终止后 thread2 start 时JVM会复用 thread1 的内核线程给 thread2
                  */
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread1").start();

        // 睡眠3s钟保证线程thread1创建对象完成
        Thread.sleep(3000);
        log.debug("打印thread1，list中第20个对象的对象头：");
        log.debug((ClassLayout.parseInstance(list.get(19)).toPrintable()));

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                Object obj = list.get(i);
                /**
                 * thread2 去竞争已偏向未锁定的对象锁会导致偏向锁撤销升级为轻量级锁
                 *  - 偏向锁撤销 -> 锁对象对应的 class 的偏向锁计数器会 +1
                 *  -  当偏向锁计数器达到 阈值（默认20） 时（实际测试出来是18），会触发批量重偏向
                 *  - 批量重偏向：锁对象的 class 的 epoch 值会 +1，并从 JVM 线程栈中找到所有该class 正处于加锁状态的偏向锁，将其epoch值更新为 class 的 epoch 值
                 *      - 也就是说这批正处于加锁状态的偏向锁不需要 CAS 改 threadId，只需要改 epoch 值
                 *      - 对于 epoch 落后的偏向锁，再次被获得锁时，会通过 CAS 重新偏向给当前线程并更新 epoch 值
                  */
                synchronized (obj) {
                    if (i >= 15 && i <= 21 || i >= 38) {
                        // 第 18、19、20、21 的对象锁状态以及 epoch 值
                        log.debug("thread2-第" + (i + 1) + "次加锁执行中\t" +
                                ClassLayout.parseInstance(obj).toPrintable());
                    }
                }
                if ( i == 19) {
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

        LockSupport.park();
    }

}
