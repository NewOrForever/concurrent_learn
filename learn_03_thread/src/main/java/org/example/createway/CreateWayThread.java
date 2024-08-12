package org.example.createway;

import java.util.concurrent.TimeUnit;

/**
 * ClassName:CreateWayThread
 * Package:org.example.createway
 * Description: 创建线程的方式
 * 1. 继承 Thread 类/使用 Thread 类
 * 2. 实现 Runnable 接口
 * 3. 实现 Callable 接口
 * 4. 使用 lambda 表达式
 *
 * @Date:2024/8/6 13:41
 * @Author:qs@1.com
 */
public class CreateWayThread extends Thread {
    public static void main(String[] args) throws InterruptedException {
        CreateWayThread createWayThread = new CreateWayThread();
        createWayThread.start();

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("使用Thread类的方式创建线程");
            }
        };
        thread.start();

        new Thread(() -> System.out.println("使用 lambda 表达式创建线程")).start();


        TimeUnit.SECONDS.sleep(10);
    }

    @Override
    public void run() {
        System.out.println("继承 Thread 类的方式创建线程");
    }

}
