package com.example.list;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * ClassName:LinkedListTest
 * Package:com.example.list
 * Description: LinkedList 源码分析
 * 底层数据结构是链表：添加、删除元素效率高，查询元素效率低
 *
 * @Date:2024/2/19 10:31
 * @Author:qs@1.com
 */
public class LinkedListTest {
    public static void main(String[] args) {
        /**
         * LinkedList 是双向链表
         * 1. 链表的头节点是 first
         * 2. 链表的尾节点是 last
         * 3. 链表的大小是 size
         * 4. 链表的节点是 {@link LinkedList.Node}
         *      - 节点的前驱是 prev
         *      - 节点的后继是 next
         *      - 节点的值是 item
         */
        LinkedList<String> list = new LinkedList<>();
        /**
         * 添加元素 - 添加到链表的尾部
         * @see LinkedList#linkLast(Object)
         * 1. 如果链表为空，新节点的前驱和后继都是 null
         * 2. 如果链表不为空，新节点的前驱是 last，后继是 null
         */
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        String peek = list.peek();
        System.out.println("获取链表头但不删除: " + peek);
        String poll = list.poll();
        System.out.println("获取链表头并删除，没有节点返回空: " + poll);
        String pop = list.pop();
        System.out.println("获取链表头并删除，没有节点抛异常: " + pop);
        list.push("ddd");

        /**
         * @see java.util.LinkedList.ListItr
         */
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println("next is :" + next);
            /**
             * 从链表中删除元素
             * @see LinkedList#unlink(LinkedList.Node)
             */
            iterator.remove();
        }
    }
}
