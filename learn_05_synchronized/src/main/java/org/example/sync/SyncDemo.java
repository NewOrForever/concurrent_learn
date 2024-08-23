package org.example.sync;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:SyncDemo
 * Package:org.example.sync
 * Description:
 *
 * @Date:2024/8/15 17:28
 * @Author:qs@1.com
 */
@Slf4j
public class SyncDemo {

    private static int counter = 0;

    private static String lock = "";

    /* 会有线程安全问题
    public static void increment() {
        counter++;
    }

    public static void decrement() {
        counter--;
    }*/

   /* // 方式一：使用 synchronized 修饰方法，静态方法锁住的是类对象
    public synchronized static void increment() {
        counter++;
    }

    public synchronized static void decrement() {
        counter--;
    }*/

    // 方式二：使用 synchronized 代码块，锁住的是 lock 这个实例对象
    public static void increment() {
        synchronized (lock) {
            counter++;
        }
    }

    public static void decrement() {
        synchronized (lock) {
            counter--;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                increment();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                decrement();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        //思考： counter=？
        log.info("{}", counter);
    }
}

