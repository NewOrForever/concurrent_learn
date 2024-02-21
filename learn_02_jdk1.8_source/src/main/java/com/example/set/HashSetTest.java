package com.example.set;

import java.util.HashSet;
import java.util.Iterator;

/**
 * ClassName:HashSetTest
 * Package:com.example.set
 * Description: HashSet 源码分析
 * 底层数据结构是哈希表，基于HashMap 实现，元素是无序的，不允许重复
 *
 * @Date:2024/2/19 13:18
 * @Author:qs@1.com
 */
public class HashSetTest {
    public static void main(String[] args) {
        /**
         * HashSet 是基于 HashMap 实现的
         * 1. HashSet 的底层数据结构是 HashMap
         * 2. HashSet 的元素是 HashMap 的 key
         * 3. HashMap 的 value 是一个静态常量 {@link HashSet#PRESENT} = new Object()
         * 4. HashSet 的 add 方法是调用 HashMap 的 put 方法
         * 5. HashSet 的 remove 方法是调用 HashMap 的 remove 方法
         * 6. HashSet 的 size 方法是调用 HashMap 的 size 方法
         * 7. HashSet 的 iterator 方法是调用 HashMap 的 keySet 方法
         * 8. HashSet 的 contains 方法是调用 HashMap 的 containsKey 方法
         * 9. HashSet 的 clear 方法是调用 HashMap 的 clear 方法
         */
        HashSet<String> set = new HashSet<>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println("next is :" + next);
            iterator.remove();
        }
    }
}
