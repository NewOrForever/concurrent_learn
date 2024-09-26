package org.example.AQS.blockingqueue;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * ClassName:PriorityBlockingQueueDemo
 * Package:org.example.AQS.blockingqueue
 * Description: PriorityBlockingQueue 示例
 * PriorityBlockingQueue 是一个支持优先级的无界阻塞队列，它的特点如下：
 * - 无界：PriorityBlockingQueue 是一个无界队列，它不会因为容量不足而抛出异常
 * - 优先级：PriorityBlockingQueue 是一个支持优先级的队列，元素按照优先级顺序被移除，该队列不允许 null 元素
 * - 阻塞
 * - 入队：不阻塞
 * - 出队：当队列为空时，从队列中获取元素的操作将会被阻塞，直到队列中有可用元素为止
 *
 * @Date:2024/9/24 16:28
 * @Author:qs@1.com
 */
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        // 创建优先级阻塞队列 - Comparator为null,自然排序
        // PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>(5);
        // 自定义Comparator - 降序
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>(5, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        Random random = new Random();
        System.out.println("put 操作");
        for (int i = 0; i < 10; i++) {
            int j = random.nextInt(100);
            System.out.println(j + "  ");
            priorityBlockingQueue.put(j);
        }

        System.out.println("take 操作");
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(priorityBlockingQueue.take() + "  ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
