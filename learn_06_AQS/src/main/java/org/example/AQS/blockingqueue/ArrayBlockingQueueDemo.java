package org.example.AQS.blockingqueue;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ClassName:ArrayBlockingQueueDemo
 * Package:org.example.AQS.blockingqueue
 * Description: ArrayBlockingQueue 示例
 * ArrayBlockingQueue 是基于数组实现的有界阻塞队列，它的特点如下：
 * - 有界：队列容量是固定的，不能动态扩容
 * - 先进先出：遵循先进先出的原则
 * - 阻塞
 *      - 当队列为空时，从队列中获取元素的操作将会被阻塞，直到队列中有可用元素为止
 *      - 当队列已满时，往队列中添加元素的操作将会被阻塞，直到队列中有空闲的位置为止
 * - 阻塞队列内部使用锁和条件变量来实现线程的阻塞和唤醒
 * - 读写操作使用同一把锁，读写不能并行
 *
 * 这里以生产者消费者模型为例，演示 ArrayBlockingQueue 的基本使用
 *
 * @Date:2024/9/14 13:54
 * @Author:qs@1.com
 */
public class ArrayBlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1024);

        Producer producer = new Producer(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);

        new Thread(producer, "thread-producer").start();
        new Thread(consumer, "thread-consumer").start();
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;

        public Producer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    blockingQueue.put(i);
                    System.out.println(Thread.currentThread().getName() + " - 生产数据：" + i);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> blockingQueue;

        public Consumer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Integer data = blockingQueue.take();
                    System.out.println(Thread.currentThread().getName() + " - 消费数据：" + data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

