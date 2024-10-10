package org.example.designpattern.balking;

/**
 * ClassName:Singleton
 * Package:org.example.designpattern.balking
 * Description: balking 设计模式
 * 什么是 balking 设计模式？
 * - balking 设计模式是一种多线程设计模式，用于处理一次性操作的场景
 * - 该模式的核心思想是：如果一个线程发现另一个线程已经开始了一次性操作，那么当前线程就放弃对这个操作的请求，转而做其他事情
 * - 该模式的实现方式有很多种，比如：使用 synchronized 关键字、使用 ReentrantLock 锁、使用信号量等
 * - balking 设计模式的应用场景：文件保存、线程中断、线程终止等
 * - balking 设计模式的优点：简单、易于理解、易于实现
 *
 * 这里以一个简单的单例模式为例，演示 balking 设计模式的实现
 * @Date:2024/10/10 10:25
 * @Author:qs@1.com
 */
public class Singleton {
    private static volatile Singleton instance;

    // 私有化构造方法
    private Singleton() {
    }

    public static Singleton getInstance() {
        // 双重检查
        if (instance == null) {
            synchronized (Singleton.class) {
                // 获取锁之后再次检查
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // 创建线程 1
        new Thread(() -> {
            Singleton instance = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() + "：" + instance);
        }, "线程 1").start();

        // 创建线程 2
        new Thread(() -> {
            Singleton instance = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() + "：" + instance);
        }, "线程 2").start();

        // 创建线程 3
        new Thread(() -> {
            Singleton instance = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() + "：" + instance);
        }, "线程 3").start();
    }

}
