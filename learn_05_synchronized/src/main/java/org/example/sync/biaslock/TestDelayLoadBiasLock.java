package org.example.sync.biaslock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * ClassName:TestDelayLoadBiasLock
 * Package:org.example.sync.biaslock
 * Description: 测试 jvm 启动时延迟加载偏向锁
 *
 * @Date:2024/8/17 10:30
 * @Author:qs@1.com
 */
@Slf4j
public class TestDelayLoadBiasLock {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
        Thread.sleep(4000);
        /**
         * //关闭延迟开启偏向锁
         * -XX:BiasedLockingStartupDelay=0
         * //禁止偏向锁
         * -XX:-UseBiasedLocking
         * //启用偏向锁
         * ‐XX:+UseBiasedLocking
         * jvm 默认延迟开启偏向锁 - 延迟时间 4s
         * 4s 后已经创建的对象不会开启偏向锁
         * 4s 后对每个新创建的对象都会开启偏向锁
         */
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
        log.debug(ClassLayout.parseInstance(new Object()).toPrintable());
    }
}
