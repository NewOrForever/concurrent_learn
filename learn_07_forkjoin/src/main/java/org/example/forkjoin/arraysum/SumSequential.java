package org.example.forkjoin.arraysum;

import org.example.forkjoin.util.Utils;

/**
 * ClassName:SumSequential
 * Package:org.example.forkjoin.arraysum
 * Description: 单线程计算1亿个整数的和
 *
 * @Date:2024/9/26 14:29
 * @Author:qs@1.com
 */
public class SumSequential {
    public static void main(String[] args) {
        // 初始化一个数组
        int[] arr = Utils.buildDefaultIntArray(100000000);
        // 计算数组的和
        System.out.printf("The array length is: %d\n", arr.length);
        long start = System.currentTimeMillis();
        long result = sum(arr);
        System.out.println("The result is: " + result + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static long sum(int[] arr) {
        return SumUtils.sumRange(arr, 0, arr.length);
    }

}
