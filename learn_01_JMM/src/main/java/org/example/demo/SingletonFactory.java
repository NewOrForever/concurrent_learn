package org.example.demo;

/**
 * ClassName:SingletonFactory
 * Package:org.example.demo
 * Description: 这也是一个对于指令重排序的测试 <br/>
 * 共享变量临界区重排序测试 - 没法演示将现象演示出来 <br/>
 * 对象创建过程中的指令重排序 <br/>
 *
 * @Date:2024/2/7 10:29
 * @Author:qs@1.com
 */
public class SingletonFactory {
    // volatile
    private static SingletonFactory myInstance;

    public static SingletonFactory getMyInstance() {
        if (myInstance == null) {
            synchronized (SingletonFactory.class) {
                if (myInstance == null) {
                    /**
                     * 对象的创建过程：
                     * 1. 开辟一块内存空间
                     * 2. 初始化对象
                     * 3. 将对象指向内存空间
                     *
                     * 由于指令重排序，可能会导致对象的创建过程变为：
                     * 1. 开辟一块内存空间
                     * 3. 将对象指向内存空间
                     * 2. 初始化对象
                     * 从而导致 myInstance 不为 null，但是对象还没有初始化完成
                     * 所以需要使用 volatile 修饰 myInstance
                     */
                    myInstance = new SingletonFactory();
                }
            }
        }
        return myInstance;
    }

    public static void main(String[] args) {
        SingletonFactory.getMyInstance();
    }
}
