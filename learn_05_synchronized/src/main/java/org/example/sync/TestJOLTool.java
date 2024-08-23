package org.example.sync;

import org.openjdk.jol.info.ClassLayout;
import sun.misc.Unsafe;

/**
 * ClassName:TestJOLTool
 * Package:org.example.sync
 * Description: JOL 工具测试
 * JOL（Java Object Layout）是一个开源的，用于查看对象内存布局的工具
 * 关闭指针压缩：-XX:-UseCompressedOops
 * 开启指针压缩：-XX:+UseCompressedOops，jdk8 默认开启，不需要手动设置
 *
 * 打印出的字节是小端存储的，即低位字节在前，高位字节在后
 *
 * @Date:2024/8/16 16:18
 * @Author:qs@1.com
 */
public class TestJOLTool {
    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        JolTestEntity entity = new JolTestEntity();
        System.out.println(ClassLayout.parseInstance(entity).toPrintable());

        Object test = new Test();
        System.out.println(ClassLayout.parseInstance(test).toPrintable());

        Unsafe unsafe = UnsafeFactory.getUnsafe();
        long offset = UnsafeFactory.getFieldOffset(unsafe, Test.class, "p");
        System.out.println("p 字段的内存偏移量：" + offset);

        Entity entity1 = new Entity();
        System.out.println(ClassLayout.parseInstance(entity1).toPrintable());
        long entityOffset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
        System.out.println("x 字段的内存偏移量：" + entityOffset);

    }
}

class JolTestEntity {
    int x;
    long y;
    boolean z;
    Integer a;
    Long b;
    Entity entity;
    boolean c;
}

class Test{
    private long p;
}
