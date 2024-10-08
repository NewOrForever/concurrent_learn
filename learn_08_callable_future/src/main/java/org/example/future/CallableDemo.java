package org.example.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * ClassName:CallableDemo
 * Package:future
 * Description:
 *
 * @Date:2024/9/30 9:49
 * @Author:qs@1.com
 */
public class CallableDemo {
    public static void main(String[] args) throws Exception {
        // Runnable 方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("通过Runnable方式执行任务");
            }
        }).start();

        // Callable 方式
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("通过Callable方式执行任务");
                Thread.sleep(3000);
                return "--------------> 返回任务结果";
            }
        });
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }

}
