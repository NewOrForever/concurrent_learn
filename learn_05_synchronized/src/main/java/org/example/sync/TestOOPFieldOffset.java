package org.example.sync;

import org.openjdk.jol.info.ClassLayout;
import sun.misc.Unsafe;

/**
 * ClassName:TestOOPFieldOffset
 * Package:org.example.sync
 * Description:
 *
 * @Date:2024/8/16 16:01
 * @Author:qs@1.com
 */
public class TestOOPFieldOffset {
    public static void main(String[] args) {
        /**
         * OOP 对象字节偏移量计算
         * 开启指针压缩：-XX:+UseCompressedOops -> 默认开启 ---> klass pointer 4 byte
         *  ===> mark word 8 byte + klass pointer 4 byte + 0byte  数组长度 + instance data 4 byte + 不需要对齐 = 16 byte
         *  >>>> x 字段的内存偏移量：12
         * 关闭指针压缩：-XX:-UseCompressedOops ---> klass pointer 8 byte -
         *  ===> mark word 8 byte + klass pointer 8 byte + 0byte  数组长度 + instance data 4 byte + 4byte 对齐 = 24 byte
         *  >>>> x 字段的内存偏移量：16
         */
        Unsafe unsafe = UnsafeFactory.getUnsafe();
        long offset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
        System.out.println("x 字段的内存偏移量：" + offset);

        // Object obj = new Object(); 该对象的内存偏移量 >>> 16
        Object obj = new Object();
         System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

}

class Entity {
    int x;
    int y;
}
