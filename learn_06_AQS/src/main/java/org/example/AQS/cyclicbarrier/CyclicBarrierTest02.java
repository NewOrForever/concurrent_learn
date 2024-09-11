package org.example.AQS.cyclicbarrier;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:CyclicBarrierTest02
 * Package:org.example.AQS.cyclicbarrier
 * Description: 测试 CyclicBarrier 的应用场景：屏障可以重复使用，支持类似人满发车的场景
 *
 * @Date:2024/9/9 16:43
 * @Author:qs@1.com
 */
public class CyclicBarrierTest02 {


    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger(0);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5,
                1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                r -> new Thread(r, count.incrementAndGet() + "号"),
                new ThreadPoolExecutor.AbortPolicy());
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("裁判：比赛开始~~");
        });

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(new Runner(cyclicBarrier));
        }

        threadPoolExecutor.shutdown();
    }

    static class Runner extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Runner(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                int sleepMills = ThreadLocalRandom.current().nextInt(1000);
                Thread.sleep(sleepMills);
                System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时： " + sleepMills + "ms" +
                        "，已就位人数" + (cyclicBarrier.getNumberWaiting() + 1));
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
