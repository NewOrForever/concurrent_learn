package org.example.createway;

import java.util.concurrent.TimeUnit;

/**
 * ClassName:VisibilityTest
 * Package:com.learn.concurrent_program
 * Description: JMM 三大特性：可见性、原子性、有序性 <br/>
 * 这里对于 <b>可见性</b> 进行测试 <br/>
 * @Date:2023/4/21 16:53
 * @Author:qs@1.com
 */
public class VisibilityTest07 {
    /**
     * 方案七
     *
     */
    private boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityTest07 test = new VisibilityTest07();

        Thread threadA = new Thread(() -> test.load(), "ThreadA");
        threadA.start();

        Thread.sleep(1000);

        Thread threadB = new Thread(() -> test.refresh(), "ThreadB");
        threadB.start();

    }

    private void refresh() {
        flag = false;
        System.out.println(Thread.currentThread().getName() + "线程已将 flag 修改为：" + flag);
    }

    private void load() {
        System.out.println(Thread.currentThread().getName() + "线程开始执行 ......");
        int i = 0;
        while (flag) {
            i++;

//            shortWait(1000000); // 可以
//            shortWait(1000);       // 不可以

            try {
                /**
                 * 内存屏障
                 */
                // Thread.sleep(1);
//                TimeUnit.NANOSECONDS.sleep(1000);         // 可以
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(Thread.currentThread().getName() + "线程跳出循环，i=" + i);
    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }

}
