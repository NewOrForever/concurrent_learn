package org.example.AQS.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName:CustomCache
 * Package:org.example.AQS.readwritelock
 * Description: 自定义缓存 - 使用读写锁保证线程安全
 *
 * @Date:2024/9/11 16:55
 * @Author:qs@1.com
 */
@Slf4j
public class CustomCache {
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Map<String, Object> map = new HashMap<>();
    private static final ReentrantReadWriteLock.ReadLock rLock = readWriteLock.readLock();
    private static final ReentrantReadWriteLock.WriteLock wLock = readWriteLock.writeLock();

    // 获取key对应的value
    public static Object get(String key) {
        rLock.lock();
        try {
            return map.get(key);
        } finally {
            rLock.unlock();
        }
    }

    // 设置key对应的value并返回旧的value
    public static Object put(String key, Object value) {
        wLock.lock();
        try {
            return map.put(key, value);
        } finally {
            // 测试用
            log.debug(Thread.currentThread().getName() + " - 写入数据完成 - key: " + key + ", value: " + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            wLock.unlock();
        }
    }

    // 清空缓存
    public static void clear() {
        wLock.lock();
        try {
            map.clear();
        } finally {
            wLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                String key = "key00" + temp;
                System.out.println(Thread.currentThread().getName() + "开始写入数据 - key: " + key + ", value: " + temp);

                put(key, temp);
            }, "thread" + i).start();
        }

        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                String key = "key00" + temp;
                Object o = get(key);
                System.out.println(Thread.currentThread().getName() + "获取数据 - key: " + key + ", value: " + o);
            }, "thread" + i).start();
        }

        Thread.sleep(100000);
    }

}
