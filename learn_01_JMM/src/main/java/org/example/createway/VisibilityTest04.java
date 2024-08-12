package org.example.createway;

/**
 * ClassName:VisibilityTest
 * Package:com.learn.concurrent_program
 * Description: JMM 三大特性：可见性、原子性、有序性 <br/>
 * 这里对于 <b>可见性</b> 进行测试 <br/>
 * @Date:2023/4/21 16:53
 * @Author:qs@1.com
 */
public class VisibilityTest04 {
    /**
     * 方案四：释放时间片，切换上下文，加载上下文
     * Thread.yield();
     */
    private boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityTest04 test = new VisibilityTest04();

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
            /**
             * 释放时间片，上下文切换，加载上下文
             */
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + "线程跳出循环，i=" + i);
    }
}
