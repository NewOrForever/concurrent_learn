package org.example.designpattern.twophasetermination;

/**
 * ClassName:MonitorProxy02
 * Package:org.example.designpattern.twophasetermination
 * Description: 优雅的终止线程 - 两阶段终止模式
 * 自定义中断标记
 *
 * @Date:2024/10/10 10:00
 * @Author:qs@1.com
 */
public class MonitorProxy02 {
    // 采集线程
    private Thread rptThread;
    private boolean started = false;
    // 自定义中断标记
    private volatile boolean terminated = false;

    // 启动采集功能
    public synchronized void start() {
        // 不允许重复启动
        if (started) {
            return;
        }
        started = true;
        rptThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !terminated) {
                // 省略采集、回传实现
                report();
                try {
                    // 每隔两秒钟采集、回传一次数据
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("线程感知到中断并会抛出 InterruptedException 异常，同时清除中断标记，当前线程中断标记：" + Thread.currentThread().isInterrupted());
                    /**
                     * 这里不需要重新设置线程的中断标记，为什么？
                     * 因为自定义了一个中断标记 terminated
                     * - 在 stop 方法中设置了该标记为 true
                     * - 在 while 循环中会检查该标记，如果为 true 则会终止线程
                     * 所以不需要重新设置线程的中断标记
                     */
                    // Thread.currentThread().interrupt();
                }
            }
            // 执行到此处说明线程马上终止
            started = false;
        });
        rptThread.start();
    }

    public synchronized void stop() {
        // 设置自定义中断标记
        terminated = true;
        // 设置线程中断标记
        rptThread.interrupt();

    }
    private void report() {
        System.out.println("采集数据");
    }

    public static void main(String[] args) {
        MonitorProxy02 monitorProxy = new MonitorProxy02();
        // 启动采集功能
        monitorProxy.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 终止采集功能
        monitorProxy.stop();
    }

}
