package org.example.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * ClassName:FutureTaskDemo
 * Package:future
 * Description: FutureTask 使用 Callable 方式执行任务
 *
 * @Date:2024/9/30 10:06
 * @Author:qs@1.com
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 构建 FutureTask
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        // FutureTask 作为 Runnable 入参
        new Thread(futureTask).start();

        System.out.println("myCallable 运行结果：" + futureTask.get());
    }

    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            Thread.sleep(3000);
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            return sum;
        }
    }

}
