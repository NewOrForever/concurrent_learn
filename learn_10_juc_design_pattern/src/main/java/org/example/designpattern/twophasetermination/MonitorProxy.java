package org.example.designpattern.twophasetermination;

/**
 * ClassName:MonitorProxy
 * Package:org.example.designpattern.twophasetermination
 * Description: 优雅的终止线程 - 两阶段终止模式
 *
 * @Date:2024/10/10 10:00
 * @Author:qs@1.com
 */
public class MonitorProxy {
    // 采集线程
    private Thread rptThread;
    private boolean started = false;

    // 启动采集功能
    public synchronized void start() {
        // 不允许重复启动
        if (started) {
            return;
        }
        started = true;
        rptThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // 省略采集、回传实现
                report();
                try {
                    // 每隔两秒钟采集、回传一次数据
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("线程感知到中断并会抛出 InterruptedException 异常，同时清除中断标记，当前线程中断标记：" + Thread.currentThread().isInterrupted());
                    /**
                     * 重新设置线程的中断标记
                     * 因为在抛出 InterruptedException 异常时会清除中断标记，将中断标记设置成 false，所以需要重新设置中断标记
                     * 否则 !Thread.currentThread().isInterrupted() 该判断条件就是 true，线程会继续执行，这样就会导致线程无法正确停止
                     */
                    Thread.currentThread().interrupt();
                }
            }
            // 执行到此处说明线程马上终止
            started = false;
        });
        rptThread.start();
    }

    public synchronized void stop() {
        // 设置线程中断标记
        rptThread.interrupt();

    }
    private void report() {
        System.out.println("采集数据");
    }

    public static void main(String[] args) {
        MonitorProxy monitorProxy = new MonitorProxy();
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
