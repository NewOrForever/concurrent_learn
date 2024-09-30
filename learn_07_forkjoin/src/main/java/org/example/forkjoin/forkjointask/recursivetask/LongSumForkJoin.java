package org.example.forkjoin.forkjointask.recursivetask;

import org.example.forkjoin.arraysum.SumUtils;
import org.example.forkjoin.util.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * ClassName:LongSum
 * Package:org.example.forkjoin.forkjointask.recursivetask
 * Description: 使用 RecursiveTask 任务实现 Fork/Join 框架
 *
 * @Date:2024/9/27 8:57
 * @Author:qs@1.com
 */
public class LongSumForkJoin {
    // private static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final int NCPU = 3;
    static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        int[] array = Utils.buildDefaultIntArray(100000000);

        /**
         * 单线程计算
         */
        long start = System.currentTimeMillis();
        long sum = SumUtils.sumRange(array, 0, array.length);
        System.out.println("单线程执行 ---> Result: " + sum + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");

        /**
         * Fork/Join 框架计算
         */
        LongSumForkJoinTask task = new LongSumForkJoinTask(array);
        // 机器的核心数
        ForkJoinPool forkJoinPool = new ForkJoinPool(NCPU);
        start = System.currentTimeMillis();
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        System.out.println("fork/join 框架计算 ---> Result: " + result.get() + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");

        /**
         * 并行流计算
         */
        start = System.currentTimeMillis();
        long parallelSum = IntStream.of(array).asLongStream().parallel().sum();
        System.out.println("并行流计算 ---> Result: " + parallelSum + ", and the time cost is: " + (System.currentTimeMillis() - start) + "ms");

    }

    static class LongSumForkJoinTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10000;
        private final int[] numbers;
        private final int start;
        private final int end;

        public LongSumForkJoinTask(int[] numbers) {
            this(numbers, 0, numbers.length);
        }

        private LongSumForkJoinTask(int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            // System.out.println("count: " + count.incrementAndGet());
            int length = end - start;
            // 跳出递归的边界条件：当任务中的数组元素个数小于等于 THRESHOLD 时，直接计算结果
            if (length <= THRESHOLD) {
                return computeSequentially();
            }
            // 不满足边界条件（任务过大）继续拆分
            LongSumForkJoinTask leftTask = new LongSumForkJoinTask(numbers, start, start + length / 2);
            LongSumForkJoinTask rightTask = new LongSumForkJoinTask(numbers, start + length / 2, end);
            // 提交任务
            leftTask.fork();
            rightTask.fork();
            // 获取任务的执行结果，将阻塞当前线程直到对应的子任务完成运行并返回结果
            Long leftResult = leftTask.join();
            Long rightResult = rightTask.join();
            return leftResult + rightResult;
        }

        private long computeSequentially() {
            return SumUtils.sumRange(numbers, start, end);
        }
    }

}
