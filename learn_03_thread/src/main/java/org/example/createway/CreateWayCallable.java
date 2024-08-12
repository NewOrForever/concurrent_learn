package org.example.createway;

import java.util.concurrent.*;

/**
 * ClassName:CreateWayCallable
 * Package:org.example.createway
 * Description: 严格来说，这里创建线程的是 ExecutorService，而不是 Callable 接口
 * Callable 接口是用来在 run 方法中执行回调的
 *
 * @Date:2024/8/6 13:52
 * @Author:qs@1.com
 */
public class CreateWayCallable {
    public static void main(String[] args) {
        CallableTask callableTask = new CallableTask();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<String> future = executorService.submit(callableTask);
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
class CallableTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        return "实现 Callable 接口的方式创建线程";
    }
}
