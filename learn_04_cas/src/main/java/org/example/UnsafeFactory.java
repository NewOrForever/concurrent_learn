package org.example;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * ClassName:UnsafeFactory
 * Package:org.example
 * Description: 获取 Unsafe 实例
 *
 * @Date:2024/8/13 11:20
 * @Author:qs@1.com
 */
public class UnsafeFactory {
    /**
     * 获取 Unsafe 对象
     * @return Unsafe 对象
     */
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字段的内存偏移量
     * @param unsafe            Unsafe 对象
     * @param clazz               类
     * @param fieldName     字段名
     * @return 字段的内存偏移量
     */
    public static long getFieldOffset(Unsafe unsafe, Class<?> clazz, String fieldName) {
        try {
            return unsafe.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

}
