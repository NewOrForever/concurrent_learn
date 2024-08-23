package org.example.sync.biaslock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * ClassName:TestTraceBiasLockStatus
 * Package:org.example.sync.biaslock
 * Description: 测试追踪偏向锁状态
 *
 * @Date:2024/8/17 13:52
 * @Author:qs@1.com
 */
@Slf4j
public class TestTraceBiasLockStatus {
    public static void main(String[] args) throws InterruptedException {
        log.debug(ClassLayout.parseInstance(new Object()).toPrintable());
        // HotSpot 虚拟机在启动后有个 4s 的延迟才会对每个新建的对象开启偏向锁模式
        Thread.sleep(4000);
        Object obj = new Object();
        /**
         * 思考：如果 obj 调用了hashCode()方法，会不会导致偏向锁撤销？
         * 答：会，因为偏向锁时对象的 mark word 没法存储 hashCode 的信息，所以在调用 hashCode() 方法时会撤销偏向锁
          */
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
        //System.out.println("obj 调用 hashCode 验证偏向锁撤销");
        obj.hashCode();
        log.debug(ClassLayout.parseInstance(obj).toPrintable());

        new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());

                // 锁对象是开启了偏向锁的 obj 对象
                synchronized (obj) {
                    /**
                     * 思考：偏向锁执行过程中，调用 hashcode 会发生什么？
                     * 偏向锁执行过程中说明偏向锁已偏向给某个线程，此时调用 hashCode() 方法会导致偏向锁强制升级位重量锁
                     */
                    /*log.debug(Thread.currentThread().getName() + "获取锁未调用hashcode。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                    obj.hashCode();*/

                    /**
                     * 思考：偏向锁执行过程中，调用 wait()/notify() 方法会发生什么？
                     * notify: 偏向锁撤销并升级为轻量级锁
                     * wait: 偏向锁升级为重量锁
                     */
                   /*  log.debug(Thread.currentThread().getName() + "获取锁未调用notify。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                    obj.notify();*/
                    /*log.debug(Thread.currentThread().getName() + "获取锁未调用wait。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n"
                            + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n"
                        + ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "thread1").start();

        /*// 已偏向未锁定的对象锁调用hashCode()方法 -> 偏向锁撤销为无锁状态
        obj.hashCode();
        log.debug("已偏向未锁定的对象锁调用hashCode \n" + ClassLayout.parseInstance(obj).toPrintable());*/

        Thread.sleep(5000);
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
    }

}
