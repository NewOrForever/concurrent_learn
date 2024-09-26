package org.example.AQS.blockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClassName:LinkedBlockingQueueDemo
 * Package:org.example.AQS.blockingqueue
 * Description: LinkedBlockingQueue 示例
 * LinkedBlockingQueue 是基于链表实现的无界阻塞队列，它的特点如下：
 * - 阻塞
 *     - 当队列为空时，从队列中获取元素的操作将会被阻塞，直到队列中有可用元素为止
 *     - 当队列已满时，往队列中添加元素的操作将会被阻塞，直到队列中有空闲的位置为止
 * - 数据结构：单向链表
 * - 两把锁：putLock 和 takeLock，分别用于 put 和 take 操作，实现读写并行
 *
 * 使用 LinkedBlockingQueue 时，需要注意以下几点：
 * - 由于 LinkedBlockingQueue 是无界队列，因此在使用时需要特别注意内存溢出的问题
 *      - 建议在创建 LinkedBlockingQueue 时，指定一个合适的容量，避免队列过大导致内存溢出
 * - LinkedBlockingQueue 适用于生产者消费者模型，生产者和消费者之间的生产和消费速度不一致时，可以使用 LinkedBlockingQueue 来缓冲数据
 *
 *
 * @Date:2024/9/14 17:10
 * @Author:qs@1.com
 */
public class LinkedBlockingQueueDemo {
    public static void main(String[] args) {
        /**
         * 测试
         * add() 方法：添加元素，如果队列已满，则抛出异常
         * offer() 方法：添加元素，如果队列已满，则返回 false
         * put() 方法：添加元素，如果队列已满，则阻塞
         * poll() 方法：获取并移除队列头部元素，如果队列为空，则返回 null
         * peek() 方法：获取队列头部元素但不移除，如果队列为空，则返回 null
         * take() 方法：获取并移除队列头部元素，如果队列为空，则阻塞
         */
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(10);

        // 1. add 方法:队列已满，报java.lang.IllegalStateException: Queue full 错误
        System.out.println("-----add  方法-----");
        for (int i = 0; i < 11; i++) {
             // linkedBlockingQueue.add(String.valueOf(i));
        }
        System.out.println(linkedBlockingQueue.size());

        // 2. offer 方法，队列已满，程序正常运行，只是不再新增元素
        System.out.println("-----offer方法-----");
        for (int i = 0; i < 10; i++) {
            linkedBlockingQueue.offer(String.valueOf(i));
        }
        System.out.println(linkedBlockingQueue.size());

        System.out.println("-----poll 方法-----");
        // 3. poll 方法，弹出队顶元素，队列为空时返回null
        for (int i = 0; i < 5; i++) {
            String e = linkedBlockingQueue.poll();
            System.out.println("取出并移除元素：" + e);
        }

        System.out.println("-----peek 方法-----");
        // 4. peek 方法，返回队列顶元素，但顶元素不弹出，队列为空时返回null
        for (int i = 0; i < 5; i++) {
            String e = linkedBlockingQueue.peek();
            System.out.println("取出元素：" + e);
        }

        System.out.println("-----take 方法-----");
        // 5. take 方法，当队列为空，阻塞
        for (int i = 0; i < 5; i++) {
            String e = null;
            try {
                e = linkedBlockingQueue.take();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("取出并移除元素：" + e);
        }

        System.out.println("-----put  方法-----");
        // 6. put 方法，当队列满时，阻塞
        for (int i = 0; i < 20; i++) {
            try {
                String e = String.valueOf(i);
                linkedBlockingQueue.put(e);
                System.out.println("放入元素：" + e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("队列深度：" + linkedBlockingQueue.size());
    }

}
