package org.example.forkjoin.arraysum;

import org.example.forkjoin.util.Utils;

import java.util.concurrent.*;

/**
 * ClassName:SumMultiThread
 * Package:org.example.forkjoin.arraysum
 * Description: 多线程计算1亿个整数的和
 * 拆分数组，多线程计算，最后合并结果
 * 测试：修改拆分粒度，观察耗时情况
 *
 * @Date:2024/9/26 14:39
 * @Author:qs@1.com
 */
public class SumMultiThread {
    // 拆分数组的粒度
    private static final int SPLIT_SIZE = 10000000;

    public static void main(String[] args) throws Exception {
        // 初始化一个数组
        int[] arr = Utils.buildDefaultIntArray(100000000);
        // 获取线程数
        int tempNumThreads = arr.length / SPLIT_SIZE;
        int numThreads = arr.length % SPLIT_SIZE == 0 && tempNumThreads > 0 ? tempNumThreads : tempNumThreads + 1;
        System.out.println("核心线程数：" + numThreads);
        // 构建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        // 预热线程池
        // ((ThreadPoolExecutor)executorService).prestartAllCoreThreads();

        long start = System.currentTimeMillis();
        // 计算数组的和
        long result = sum(arr, executorService, numThreads);

        System.out.println("The result is: " + result + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");

        executorService.shutdown();
    }

    private static long sum(int[] arr, ExecutorService executorService, int numThreads) throws Exception {
        long sum = 0;
        // 任务分解
        SumTask[] tasks = new SumTask[numThreads];
        Future<Long>[] sums = new Future[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int start = i * SPLIT_SIZE;
            int end = Math.min((i + 1) * SPLIT_SIZE, arr.length);
            tasks[i] = new SumTask(arr, start, end);
            sums[i] = executorService.submit(tasks[i]);
            if (i == numThreads - 1) {
                System.out.println("最后一个线程的起始位置：" + start + "，结束位置：" + end);
            }
        }

        // 结果合并
        for (Future<Long> longFuture : sums) {
            sum += longFuture.get();
        }
        return sum;
    }


}
