package org.example.createway;

/**
 * ClassName:AtomicTest
 * Package:org.example.demo
 * Description: 原子性测试 <br/>
 * 1. volatile 对任意单个volatile变量的读/写具有原子性，但类似于volatile++这种复合操作不具有原子性
 * 2. 所以可以认为 volatile 是不能保证原子性
 *
 * @Date:2024/2/7 11:04
 * @Author:qs@1.com
 */
public class AtomicTest {
    private volatile static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    count++;
                }
            }).start();
        }

        // 等待所有线程执行完毕
        Thread.sleep(5000);

        System.out.println("count = " + count);
    }
}
