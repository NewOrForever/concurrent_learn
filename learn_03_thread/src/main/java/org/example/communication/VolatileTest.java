package org.example.communication;

/**
 * ClassName:VolatileTest
 * Package:org.example.common_method.communication
 * Description: java 线程间通信之 volatile 关键字
 * 能够保证可见性、有序性，但是不能保证原子性
 *
 * @Date:2024/8/12 15:24
 * @Author:qs@1.com
 */
public class VolatileTest {
    private static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (flag) {
                        System.out.println("turn on");
                        flag = false;
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!flag) {
                        System.out.println("turn off");
                        flag = true;
                    }
                }
            }
        }).start();
    }

}
