package org.example.sync;

import org.example.UnsafeFactory;
import sun.misc.Unsafe;

/**
 * ClassName:CasTest
 * Package:org.example
 * Description:
 *
 * @Date:2024/8/13 11:27
 * @Author:qs@1.com
 */
public class CasTest {
    public static void main(String[] args) {
        Entity entity = new Entity();

        Unsafe unsafe = UnsafeFactory.getUnsafe();
        // 获取 x 字段的内存偏移量
        long offset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
        System.out.println("x 字段的内存偏移量：" + offset);

        // 进行 CAS 操作
        boolean success;
        // 4个参数分别是：对象实例、字段的内存偏移量、字段期望值、字段新值
        success = unsafe.compareAndSwapInt(entity, offset, 0, 3);
        System.out.println(success + "\t" + entity.x);

        success = unsafe.compareAndSwapInt(entity, offset, 3, 5);
        System.out.println(success + "\t" + entity.x);

        success = unsafe.compareAndSwapInt(entity, offset, 3, 8);
        System.out.println(success + "\t" + entity.x);
    }

}


class Entity {
    int x;
}