package org.example.demo;

/**
 * ClassName:VisibilityTest
 * Package:com.learn.concurrent_program
 * Description: JMM 三大特性：可见性、原子性、有序性 <br/>
 * 这里对于 <b>可见性</b> 进行测试 <br/>
 * 这个是原始的有并发问题的版本
 * @Date:2023/4/21 16:53
 * @Author:qs@1.com
 */
public class VisibilityTest {
    private boolean flag = true;
    /**
     * 测试思路：
     *  0. 全局变量 flag 的可见性测试
     * 1. 创建两个线程 A, B
     *    线程 A 执行 load -> while(flag)，等待 1s 线程B 开始执行 refresh -> flag 更新为 false，看 A 线程的 while 循环是否能打破
     */
    public static void main(String[] args) throws InterruptedException {
        VisibilityTest test = new VisibilityTest();

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
            // TODO 业务逻辑
        }
        System.out.println(Thread.currentThread().getName() + "线程跳出循环，i=" + i);
    }
}
