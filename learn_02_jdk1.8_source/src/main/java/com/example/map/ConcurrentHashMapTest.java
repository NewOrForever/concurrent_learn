package com.example.map;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:ConcurrentHashMapTest
 * Package:com.example.map
 * Description: ConcurrentHashMap 源码分析
 * 底层数据结构是哈希表，基于数组+链表+红黑树实现，线程安全的 HashMap
 *
 * @Date:2024/2/19 17:27
 * @Author:qs@1.com
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key001", "aaa");
        concurrentHashMap.put("key002", "bbb");
        concurrentHashMap.put("key003", "ccc");

        System.out.println(tableSizeFor(16));
        System.out.println(tableSizeFor(15));
        System.out.println(tableSizeFor(100));

        testWhileTrueBreak();

    }



    static final int MAXIMUM_CAPACITY = 1 << 30;
    private static int tableSizeFor(int c) {
        /**
         * 减一是为了保证 c 本身是 2 的幂次方时，返回的是 c
         * 如果不减 1 的话，c 本身是 2 的幂次方时，返回的是 c * 2，这样就会浪费空间
         *
         * 下面的移位操作是为了将高位的1 都移动到最低位，然后再加 1，这样就可以保证返回的是 2 的幂次方
         * 例如：c = 15 = 1111 -> n = c - 1 =14 = 1110
         * 1110 | 0111 = 1111 -> 1111 | 0011 = 1111 -> 1111 | 0000 = 1111 -> 1111 | 0000 = 1111 => 15 + 1 = 16
         * 例如：c = 100 = 1100100 -> n = c - 1 = 99 = 1100011
         * 1100011 | 0110001 = 1110011 -> 1110011 | 0001100 = 1111111 -> 1111111 | 0000111 = 1111111 -> 1111111 | 0000000 = 1111111 => 127 + 1 = 128
         *
         * 总结：实际就是最高位的1后面全部变成1
         */
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    public static int testWhileTrueBreak() {
        int i = 0;
        while (true) {
            i++;
            if (i == 10) {
                // return 也能跳出循环
                return i;
            }
            System.out.println(i);
        }
    }
}
