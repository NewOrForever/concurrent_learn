package org.example.AQS.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:ConditionTest
 * Package:org.example.AQS
 * Description: AQS - Condition 条件队列使用测试
 *
 * @Date:2024/8/26 14:46
 * @Author:qs@1.com
 */
@Slf4j
public class ConditionTest {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName() + " - 等待信号");
                condition.await();
                log.debug(Thread.currentThread().getName() + " - 收到信号");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread01").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName() + " - 开始发送信号");
                Thread.sleep(3000);
                condition.signal();
                log.debug(Thread.currentThread().getName() + " - 发送信号完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread02").start();
    }

}
