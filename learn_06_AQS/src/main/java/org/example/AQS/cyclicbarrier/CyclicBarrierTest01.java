package org.example.AQS.cyclicbarrier;

import java.util.concurrent.*;

/**
 * ClassName:CyclicBarrierTest01
 * Package:org.example.AQS.cyclicbarrier
 * Description: 测试 CyclicBarrier 的应用场景：多线程计算数据，最后合并计算结果
 *
 * @Date:2024/9/9 16:18
 * @Author:qs@1.com
 */
public class CyclicBarrierTest01 {
    private final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    // 创建一个 CyclicBarrier，初始值为 3
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
        int result = map.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("3人总成绩：" + result);
    });
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        CyclicBarrierTest01 cyclicBarrierTest01 = new CyclicBarrierTest01();
        cyclicBarrierTest01.calculate();
    }

    private void calculate() {
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                // 获取学生平均成绩
                int score = (int) (Math.random() * 40 + 60);
                map.put(Thread.currentThread().getName(), score);
                System.out.println(Thread.currentThread().getName() + " - 成绩：" + score);
                try {
                    // 执行完运行await()，等待所有学生成绩都计算完毕
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
    }

}
