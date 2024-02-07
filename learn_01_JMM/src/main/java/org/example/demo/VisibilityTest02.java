package org.example.demo;

/**
 * ClassName:VisibilityTest
 * Package:com.learn.concurrent_program
 * Description: JMM 三大特性：可见性、原子性、有序性 <br/>
 * 这里对于 <b>可见性</b> 进行测试 <br/>
 * @Date:2023/4/21 16:53
 * @Author:qs@1.com
 */
public class VisibilityTest02 {
    private boolean flag = true;

    /**
     * 方案2：将局部变量 i 换成全局变量 count （int 类型 + volatile 修饰）
     * 或者 直接 Integer 修饰 count
     */
    // private volatile int count = 0;
    private Integer count = 0;

    public static void main(String[] args) throws InterruptedException {
        VisibilityTest02 test = new VisibilityTest02();

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
        while (flag) {
            count++;
            // TODO 业务逻辑
        }
        System.out.println(Thread.currentThread().getName() + "线程跳出循环，count=" + count);
    }
}
