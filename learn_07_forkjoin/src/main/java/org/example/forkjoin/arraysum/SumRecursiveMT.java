package org.example.forkjoin.arraysum;

import org.example.forkjoin.util.Utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:SumMultiThread
 * Package:org.example.forkjoin.arraysum
 * Description: 使用递归方式实现多线程计算1亿个整数的和
 *
 * @Date:2024/9/26 14:39
 * @Author:qs@1.com
 */
public class SumRecursiveMT {
    public static AtomicInteger count = new AtomicInteger(0);
    public static void main(String[] args) throws Exception {
        // 初始化一个数组
        int[] arr = Utils.buildDefaultIntArray(100000000);
        long start = System.currentTimeMillis();
        // 计算数组的和
        long result = sum(arr);

        System.out.println("The result is: " + result + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");
    }

    private static long sum(int[] arr) throws Exception {
        // 思考： 用 Executors.newFixedThreadPool可以吗？
        // 定长线程的饥饿，导致缺少线程执行任务，从而导致死锁
        // 因为在这里持有任务的线程需要等待子任务的执行结果，如果线程池中的线程不够用，就会导致没有线程去执行子任务，从而导致死锁
        // ExecutorService executorService = Executors.newFixedThreadPool(20);
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 递归任务求和
        SumRecursiveTask task = new SumRecursiveTask(executorService, arr, 0, arr.length);
        long result = executorService.submit(task).get();
        executorService.shutdown();
        return result;
    }


    static class SumRecursiveTask implements Callable<Long> {
        // 拆分数组的粒度
        public static final int SPLIT_SIZE = 10000000;

        private int[] arr;
        private int start;
        private int end;
        private ExecutorService executorService;

        public SumRecursiveTask(ExecutorService executorService, int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
            this.executorService = executorService;
        }

        @Override
        public Long call() throws Exception {
            System.out.println("线程：" + Thread.currentThread().getName() + "，起始位置：" + start + "，结束位置：" + end);
            // 如果拆分的数组小于拆分粒度（说明是最后一次拆分得到的数组），直接计算
            // 否则，继续递归拆分
            // 这是终止递归的条件（实际就是计算到了最后一次拆分的数组）
            if (end - start <= SPLIT_SIZE) {
                System.out.println("拆分出最小任务数：" + count.addAndGet(1));
                return SumUtils.sumRange(arr, start, end);
            } else {
                /**
                 * 将大任务拆分成两个小任务 -> 递归
                 */
                int mid = start + (end - start) / 2;
                SumRecursiveTask left = new SumRecursiveTask(executorService, arr, start, mid);
                SumRecursiveTask right = new SumRecursiveTask(executorService, arr, mid, end);
                Future<Long> leftResult = executorService.submit(left);
                Future<Long> rightResult = executorService.submit(right);
                return leftResult.get() + rightResult.get();
            }
        }
    }

}
