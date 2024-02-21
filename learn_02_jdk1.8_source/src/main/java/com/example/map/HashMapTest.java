package com.example.map;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:HashMapTest
 * Package:com.example.map
 * Description: HashMap 源码分析
 * 底层数据结构是哈希表，基于数组+链表+红黑树实现，<key, value> 键值对，key 可以为null，相同的 key 会覆盖
 *
 * @Date:2024/2/19 13:58
 * @Author:qs@1.com
 */
public class HashMapTest {
    public static void main(String[] args) throws InterruptedException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lies", "aaa");
        hashMap.put("foes", "bbb");

        /**
         * (n - 1) & hash -》hash % n
         * n 为 2 的幂次方 -> (n - 1) & hash = hash % n -> HashMap 的容量是 2 的幂次方，每次扩容是原容量的 2 倍
         * n 不为 2 的幂次方 -> (n - 1) & hash != hash % n
          */
        int hash = 100; // 1100100
        int n_two_power = 16; // 10000
        int n_not_two_power = 15; // 1111
        int n_tow_power_result = (n_two_power - 1) & hash; // 1111 & 1100100 = 4
        int n_not_two_power_result = (n_not_two_power - 1) & hash; // 1110 & 1100100 = 4

        int n_two_power_mod = hash % n_two_power; // 16 * 6 + 4 = 100
        int n_not_two_power_mod = hash % n_not_two_power; // 15 * 6 + 10 = 100

        System.out.println("n_two_power_result is :" + n_tow_power_result);
        System.out.println("n_not_two_power_result is :" + n_not_two_power_result);
        System.out.println("n_two_power_mod is :" + n_two_power_mod);
        System.out.println("n_not_two_power_mod is :" + n_not_two_power_mod);
    }
}
