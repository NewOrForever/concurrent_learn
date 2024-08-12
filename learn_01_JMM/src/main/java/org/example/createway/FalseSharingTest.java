package org.example.createway;

import sun.misc.Contended;

/**
 * ClassName:FalseSharingTest
 * Package:org.example.demo
 * Description: 伪共享测试 <br/>
 *
 * T1 线程修改 x，T2 线程修改 y，x 和 y 在同一个缓存行中，会导致缓存行的竞争，从而影响性能
 * 不管x修改还是y修改，都会导致缓存行的失效，从而影响性能
 * 解决：
 * 1. 使用 @Contended 注解，jdk8 之后支持，但是需要在启动时添加 -XX:-RestrictContended 参数
 * 2. 缓存行填充，使得 x 和 y 不在同一个缓存行中
 * 3. 将数据存储在每个线程自己的缓存中（局部变量），以减少线程间的数据共享
 *
 * @Date:2024/2/5 15:58
 * @Author:qs@1.com
 */
public class FalseSharingTest {

    public static void main(String[] args) throws InterruptedException {
        test(new Pointer());
    }

    private static void test(Pointer pointer) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.x++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.y++;
            }
        });

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(pointer.x + ", " + pointer.y);

        System.out.println(System.currentTimeMillis() - start);
    }
}

class Pointer {
    // jdk8 之后支持，但是需要在启动时添加 -XX:-RestrictContended 参数
    @Contended
    volatile long x;
    // 缓存行填充
    // long p1, p2, p3, p4, p5, p6, p7;

    volatile long y;

    public Pointer() {
    }
}
