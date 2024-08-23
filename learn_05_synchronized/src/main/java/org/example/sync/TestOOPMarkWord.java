package org.example.sync;

/**
 * ClassName:TestOOPMarkWord
 * Package:org.example.sync
 * Description: 测试 OOP 的 Mark Word 结构
 *
 * @Date:2024/8/17 9:05
 * @Author:qs@1.com
 */
public class TestOOPMarkWord {
    public static void main(String[] args) {
        /**
         * 1. 对象头 Mark Word - hash 25bit
         * 使用的是 System.identityHashCode(obj) 方法来计算对象的默认哈希码值（与对象的地址相关）
         *
         * object.hashCode() 方法如果没有重写，返回的是对象的默认哈希码值和 System.identityHashCode(obj) 方法返回的值是一样的
         * 如果重写了 hashCode() 方法，返回的是重写后的哈希码，和 System.identityHashCode(obj) 方法返回的值不一样
         */
        // 方法返回的是对象的默认的哈希码值
        Object obj = new Object();
        System.out.println(obj.hashCode());
        System.out.println(System.identityHashCode(obj));

        CustomObject customObj = new CustomObject();
        System.out.println("customObj.hashCode(): " + customObj.hashCode());
        System.out.println("System.identityHashCode(customObj): " + System.identityHashCode(customObj));
    }
}

class CustomObject {
    @Override
    public int hashCode() {
        return 42; // Custom hash code
    }
}
