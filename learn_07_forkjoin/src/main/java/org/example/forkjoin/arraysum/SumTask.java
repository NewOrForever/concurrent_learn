package org.example.forkjoin.arraysum;

import java.util.concurrent.Callable;

/**
 * ClassName:SumTask
 * Package:org.example.forkjoin.arraysum
 * Description:
 *
 * @Date:2024/9/26 15:08
 * @Author:qs@1.com
 */
public class SumTask implements Callable<Long> {
    private int[] arr;
    private int start;
    private int end;

    public SumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    public Long call() {
        return SumUtils.sumRange(arr, start, end);
    }

}
