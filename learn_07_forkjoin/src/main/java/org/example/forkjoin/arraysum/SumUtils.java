package org.example.forkjoin.arraysum;

/**
 * ClassName:SumUtils
 * Package:org.example.forkjoin.arraysum
 * Description:
 *
 * @Date:2024/9/26 14:36
 * @Author:qs@1.com
 */
public class SumUtils {
    public static long sumRange(int[] arr, int start, int end) {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += arr[i];
        }
        return sum;
    }

}
