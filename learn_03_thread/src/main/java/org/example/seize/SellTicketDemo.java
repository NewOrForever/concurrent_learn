package org.example.seize;

/**
 * ClassName:SellTicketDemo
 * Package:org.example.seize
 * Description: java 线程的调度机制是抢占式调度，不是协作式调度
 * 1. 抢占式调度：优先级高的线程会抢占 CPU 的执行权，优先级低的线程会被暂停
 * 2. 协作式调度：线程执行完毕或者主动让出 CPU 的执行权，才会执行下一个线程
 * 3. 线程的优先级范围是 1-10，默认是 5
 * 4. 线程的优先级高低只是决定 CPU 的执行权的概率，不是绝对的，最终还是由操作系统决定
 *
 * 下面以卖票为例，演示线程的抢占式调度以及优先级的设置
 * @Date:2024/8/9 13:42
 * @Author:qs@1.com
 */
public class SellTicketDemo implements Runnable {
    private int ticket;

    public SellTicketDemo() {
        this.ticket = 1000;
    }

    @Override
    public void run() {
        while (ticket > 0) {
           synchronized (this) {
               if (ticket > 0) {
                   try {
                       // 线程休眠 2 毫秒，模拟线程执行过程中的耗时操作
                       // sleep 方法会让当前线程从 Running 状态进入 Timed Waiting 状态，不会释放对象锁
                       Thread.sleep(2);
                   } catch (InterruptedException e) {
                          e.printStackTrace();
                   }
                   System.out.println(Thread.currentThread().getName() + "：正在执行，余票：" + ticket--);
               }
           }
           // 线程让出 CPU 的执行权，让其他线程执行
           Thread.yield();
        }
    }

    public static void main(String[] args) {
        SellTicketDemo sellTicket = new SellTicketDemo();

        Thread t1 = new Thread(sellTicket, "窗口1");
        Thread t2 = new Thread(sellTicket, "窗口2");
        Thread t3 = new Thread(sellTicket, "窗口3");
        Thread t4 = new Thread(sellTicket, "窗口4");

        // priority优先级默认是5，最低1，最高10
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);
        t4.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

}
