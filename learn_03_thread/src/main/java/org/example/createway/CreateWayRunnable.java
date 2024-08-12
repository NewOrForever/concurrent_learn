package org.example.createway;

/**
 * ClassName:CreateWayRunnable
 * Package:org.example.createway
 * Description:
 *
 * @Date:2024/8/6 13:51
 * @Author:qs@1.com
 */
public class CreateWayRunnable {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("实现 Runnable 接口的方式创建线程");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
