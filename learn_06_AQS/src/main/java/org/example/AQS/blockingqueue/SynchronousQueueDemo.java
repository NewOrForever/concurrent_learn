package org.example.AQS.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * ClassName:SynchronousQueueDemo
 * Package:org.example.AQS.blockingqueue
 * Description: SynchronousQueue 示例
 * SynchronousQueue 是一个不存储元素的阻塞队列，它的特点如下：
 * - 不存储元素：SynchronousQueue 内部并不存储元素，每个插入操作必须等待另一个线程的对应删除操作，反之亦然
 * - 阻塞：当队列为空时，从队列中获取元素的操作将会被阻塞，直到队列中有可用元素为止
 *
 *
 * @Date:2024/9/21 13:55
 * @Author:qs@1.com
 */
public class SynchronousQueueDemo {
    public static void main(String[] args) {
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    LockSupport.park();
                    System.out.println("提前颁发许可，park 方法不会阻塞");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        LockSupport.unpark(thread);*/
        // 线程池中的应用：Executors.newCachedThreadPool()
         Executors.newCachedThreadPool();

        // SynchronousQueue 是一个不存储元素的阻塞队列
        // 1. 创建一个 SynchronousQueue 对象，默认是非公平模式
        final SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>(true);

        // 2. 启动1个生产者线程
        SynchronousQueueProducer producer = new SynchronousQueueProducer(synchronousQueue);
        new Thread(producer, "thread-producer").start();

        // 3. 启动2个消费者线程
        SynchronousQueueConsumer consumer1 = new SynchronousQueueConsumer(synchronousQueue);
        new Thread(consumer1, "thread-consumer-01").start();
        SynchronousQueueConsumer consumer2 = new SynchronousQueueConsumer(synchronousQueue);
        new Thread(consumer2, "thread-consumer-02").start();
    }

    static class SynchronousQueueProducer implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;

        public SynchronousQueueProducer(BlockingQueue<Integer> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    blockingQueue.put(i);
                    System.out.println(Thread.currentThread().getName() + " - 生产数据：" + i);
                    // 每隔2秒生产一次数据
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class SynchronousQueueConsumer implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;

        public SynchronousQueueConsumer(BlockingQueue<Integer> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Integer data = blockingQueue.take();
                    long currentTime = System.currentTimeMillis() / 1000;
                    System.out.println(Thread.currentThread().getName() + " - 消费数据：" + data + ", 当前时间：" + currentTime);
                    // 每隔5秒消费一次数据
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
