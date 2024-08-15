package org.example.cas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * ClassName:LongAccumulatorTest
 * Package:org.example.cas
 * Description: java 高并发之 LongAccumulator
 * LongAccumulator 是 LongAdder 的升级版，LongAdder 是对一个变量进行累加/减操作，而 LongAccumulator 提供了自定义的函数操作
 *
 * @Date:2024/8/15 13:17
 * @Author:qs@1.com
 */
public class LongAccumulatorTest {
    public static void main(String[] args) {
        LongAccumulator longAccumulator = new LongAccumulator((value, x) -> value + x, 0);

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        IntStream.range(1, 10).forEach(i -> executorService.submit(() -> longAccumulator.accumulate(i)));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("sum: " + longAccumulator.get());
        System.out.println("sumThenReset: " + longAccumulator.getThenReset());
        System.out.println("sum after reset: " + longAccumulator.get());

    }

}
