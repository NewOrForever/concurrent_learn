package org.example.AQS.blockingqueue.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:DelayQueueDemo
 * Package:org.example.AQS.blockingqueue.delayqueue
 * Description: DelayQueue 示例
 * DelayQueue 是一个支持延时获取元素的无界阻塞队列，队列使用 PriorityQueue 来实现，队列中的元素必须实现 Delayed 接口
 *
 * @Date:2024/9/25 14:55
 * @Author:qs@1.com
 */
public class DelayQueueDemo {
    public static void main(String[] args) {
        // 创建延时队列
        final DelayQueue<DelayElement> delayQueue = new DelayQueue<>();

        // 添加延时元素到延时队列
        new Thread(new DelayQueueProducer(delayQueue)).start();

        // 启动消费者线程
        new Thread(new DelayQueueConsumer(delayQueue)).start();
        // DelayQueueConsumer 中模拟了消费者线程处理业务耗时超过延时时间的情况
        // 此时就需要使用多个消费者线程来处理延时队列中的元素
        // 比如：现在消费者线程处理业务耗时为 2 秒，而延时队列中的延时时间为 1 秒
        // 如果只有一个消费者线程，那么延时队列中的元素将会每2秒才能被消费一次
        // 如果有多个消费者线程，那么延时队列中的元素将会每1秒就能被消费一次
        // 如果消费者线程处理业务耗时低于延时时间的情况，那么多个消费者线程也不会提高消费速度，只需要一个消费者线程即可
        // new Thread(new DelayQueueConsumer(delayQueue)).start();
        // new Thread(new DelayQueueConsumer(delayQueue)).start();
    }

    static class DelayElement implements Delayed {
        private long delayTime;
        private long expireTime;
        private String data;

        public DelayElement(long delayTime, String data) {
            this.delayTime = delayTime;
            this.expireTime = System.currentTimeMillis() + delayTime;
            this.data = data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            long d = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
            return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
        }
    }

    static class DelayQueueConsumer implements Runnable {
        private DelayQueue<DelayElement> delayQueue;

        public DelayQueueConsumer(DelayQueue<DelayElement> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DelayElement element = delayQueue.take();
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + "消费：" + element.data + "，时间：" + System.currentTimeMillis() / 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DelayQueueProducer implements Runnable {
        private DelayQueue<DelayElement> delayQueue;

        public DelayQueueProducer(DelayQueue<DelayElement> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                delayQueue.put(new DelayElement(1000 * (i + 1), "delay" + (i + 1)));
                System.out.println("生产：" + "delay" + (i + 1));
            }
        }
    }

}
