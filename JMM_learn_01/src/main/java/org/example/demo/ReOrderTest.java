package org.example.demo;

/**
 * ClassName:ReOrderTest
 * Package:org.example.demo
 * Description: 指令重排序测试 <br/>
 *
 * @Date:2024/2/5 17:22
 * @Author:qs@1.com
 */
public class ReOrderTest {
    // volatile 修饰的变量，禁止指令重排序
    private static int a = 0;
    // volatile
    private static int b = 0;
    private  static int x = 0;
    private  static int y = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        while (true) {
            i++;
            a = 0;
            b = 0;
            x = 0;
            y = 0;

            /**
             * 可能的结果：0 0 ，0 1 ，1 0 ，1 1
             *
             * 为什么会出现 0 0 的情况？
             * 指令重排序的原因
             *
             * 为什么 volatile 修饰 x 和 y 不能解决？
             * 我自己的理解：
             *      - volatile 修饰 x，volatile 写操作
             *          1. 会在写操作之前插入一个 StoreStore 屏障，禁止上面的普通写操作和下面的 volatile 写操作重排序
             *          2. 会在写操作之后插入一个 StoreLoad 屏障，禁止上面的 volatile 写操作和下面的 volatile 读/写操作重排序
             *          3. x = b 这行代码，写 volatile x 操作上面是对 b 的普通读操作，所以不会禁止重排序 ？？？？？？？？？
             */

            Thread t1 = new Thread(() -> {
                shortWait(20000);
                a = 1;
                x = b;
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("第" + i + "次：x=" + x + ", y=" + y);

            if (x == 0 && y == 0) {
                break;
            }
        }
    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}
