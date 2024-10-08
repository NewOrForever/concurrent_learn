package org.example.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:FutureTaskDemo3
 * Package:future
 * Description: FutureTask 使用案例
 * 烧水泡茶
 * 1. T1 洗水壶 -> 烧开水 -> 泡茶（获取T2任务的茶叶）
 * 2. T2 洗茶壶 -> 洗茶杯 -> 拿茶叶
 *
 * @Date:2024/10/8 9:11
 * @Author:qs@1.com
 */
public class FutureTaskDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建任务 T2 的 FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        // 创建任务 T1 的 FutureTask
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));
        // 线程 T1 执行任务 ft1
        Thread t1 = new Thread(ft1);
        t1.start();
        // 线程 T2 执行任务 ft2
        Thread t2 = new Thread(ft2);
        t2.start();

        System.out.println(ft1.get());
    }
}
class T2Task implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("T2:洗茶壶...");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("T2:洗茶杯...");
        TimeUnit.SECONDS.sleep(2);
        System.out.println("T2:拿茶叶...");
        TimeUnit.SECONDS.sleep(1);
        return "龙井";
    }
}

class T1Task implements Callable<String> {
    FutureTask<String> ft2;

    //  T1 任务需要 T2 任务的 FutureTask
    T1Task(FutureTask<String> ft2) {
        this.ft2 = ft2;
    }

    @Override
    public String call() throws Exception {
        System.out.println("T1:洗水壶...");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("T1:烧开水...");
        TimeUnit.SECONDS.sleep(15);
        // 获取 T2 任务的茶叶
        String tf = ft2.get();
        System.out.println("T1:拿到茶叶：" + tf);
        System.out.println("T1:泡茶...");
        return "上茶：" + tf;
    }
}
