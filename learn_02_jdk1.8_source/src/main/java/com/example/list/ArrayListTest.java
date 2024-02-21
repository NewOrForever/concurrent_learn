package com.example.list;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ClassName:ArrayList
 * Package:com.example.list
 * Description: ArrayList 源码分析
 * 底层数据结构是数组：添加、删除元素效率低，查询元素效率高
 *
 * @Date:2024/2/7 16:49
 * @Author:qs@1.com
 */
public class ArrayListTest {
    public static void main(String[] args) {
        /**
         * 使用<b>无参</b>构造参数创建 ArrayList 对象
         * 1. elementData 空对象为 {@link ArrayList#DEFAULTCAPACITY_EMPTY_ELEMENTDATA}
         * 2. 使用 {@link ArrayList#add(Object)} 方法添加元素时，elementData 空数组会扩容为 {@link ArrayList#DEFAULT_CAPACITY} = 10
         *
         * 使用<b>有参</b>构造参数创建 ArrayList 对象
         * 1. elementData 空对象为 {@link ArrayList#EMPTY_ELEMENTDATA}
         * 2. 使用 {@link ArrayList#add(Object)} 方法添加元素时，elementData 空数组会按照扩容因子扩容，新的容量 = 原始容量 * 1.5
         *
         * @see ArrayList#ensureCapacityInternal(int) 扩容方法
         *      -> {@link ArrayList#grow(int)}
         *      -> 扩容时会复制原数组的元素到新数组，新数组的容量 = 原始容量 * 1.5
         *
         * @see ArrayList#calculateCapacity(Object[], int) 根据空数组对象计算需要的容量
         *
         */
        ArrayList<String> list01 = new ArrayList<>();
        list01.add("aaa");
        list01.add("bbb");
        list01.add("ccc");
        ArrayList<String> list02 = new ArrayList<>();

        Iterator<String> iterator = list01.iterator();
        while (iterator.hasNext()) {
            /**
             * @see java.util.ArrayList.Itr#next()
             */
            String next = iterator.next();
            System.out.println("next is :" + next);
            iterator.remove();
            System.out.println(">>>>>>>>>>>>>>连续两次iterator 删除测试<<<<<<<<<<<<<");
            /**
             * @see java.util.ArrayList.Itr#remove()
             * 删除元素后 {@link ArrayList.Itr#lastRet} 会被重新初始化为 -1
             * 所以连续两次调用 iterator.remove() 会抛出异常 {@link java.lang.IllegalStateException}
             * 每次调用 iterator.remove() 之后，需要重新调用 iterator.next() 方法才能继续删除元素
             */
//            iterator.remove();
        }
    }
}
