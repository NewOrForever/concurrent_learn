package org.example.AQS.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ClassName:BlockingQueueDemo
 * Package:org.example.AQS.blockingqueue
 * Description: 阻塞队列示例
 * 阻塞队列是一种特殊的队列，它的特点如下：
 * - 当队列是空的时，从队列中获取元素的操作将会被阻塞，直到队列中有可用元素为止
 * - 当队列是满的时，往队列中添加元素的操作将会被阻塞，直到队列中有空闲的位置为止
 * 阻塞队列常用于生产者和消费者的场景，生产者是往队列中添加元素的线程，消费者是从队列中获取元素的线程
 * 阻塞队列的实现原理：阻塞队列内部使用锁和条件变量来实现线程的阻塞和唤醒
 * 阻塞队列的实现方式有多种，如 ArrayBlockingQueue、LinkedBlockingQueue、PriorityBlockingQueue、DelayQueue、SynchronousQueue 等
 * ArrayBlockingQueue：基于数组实现的有界阻塞队列
 * LinkedBlockingQueue：基于链表实现的有界阻塞队列
 * PriorityBlockingQueue：支持优先级排序的无界阻塞队列
 * DelayQueue：支持延时获取元素的无界阻塞队列
 * SynchronousQueue：不存储元素的阻塞队列
 * 阻塞队列的使用场景：
 * - 数据共享：多个线程之间共享数据时，可以使用阻塞队列来实现线程间的数据传递
 * - 线程同步：多个线程之间的协作时，可以使用阻塞队列来实现线程的同步
 * - 任务调度：线程池中的任务调度时，可以使用阻塞队列来实现任务的调度
 * - 消息传递：多个线程之间的消息传递时，可以使用阻塞队列来实现消息的传递
 *
 * @Date:2024/9/14 11:07
 * @Author:qs@1.com
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        /*********入队*********/
        // testAdd();
        // testOffer();
        // testPut();
        /*********出队*********/
        // testPoll();
        // testRemove();
        // testTake();
        /*********获取队列头部元素*********/
        // testPeek();
        testElement();
    }

    /**
     * add() 方法：向队列中添加元素
     * - 添加成功返回 true
     * - 如果队列已满，则抛出异常，
     */
    private static void testAdd() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        System.out.println(blockingQueue.add(1));
        System.out.println(blockingQueue.add(2));
        // 队列已满，抛出异常：java.lang.IllegalStateException: Queue full
        System.out.println(blockingQueue.add(3));
    }

    /**
     * offer() 方法：向队列中添加元素
     * - 添加成功返回 true
     * - 如果队列已满，则返回 false
     */
    private static void testOffer() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        System.out.println(blockingQueue.offer(1));
        System.out.println(blockingQueue.offer(2));
        // 队列已满，返回 false
        System.out.println(blockingQueue.offer(3));
    }

    /**
     * put() 方法：向队列中添加元素
     * - 如果队列已满，则阻塞线程直到队列空出位置
     */
    private static void testPut() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        try {
            blockingQueue.put(1);
            System.out.println("添加元素1成功");
            blockingQueue.put(2);
            System.out.println("添加元素2成功");
            // 队列已满，阻塞等待
            blockingQueue.put(3);
            System.out.println("添加元素3成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * poll() 方法：移除并返回队列头部元素
     * - 移除成功返回元素，队列为空返回 null
     */
    private static void testPoll() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(2);
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        // 队列为空，返回 null
        System.out.println(blockingQueue.poll());
    }

    /**
     * remove() 方法：移除并返回队列头部元素
     * - 移除成功返回元素，队列为空抛出异常
     */
    private static void testRemove() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(2);
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        // 队列为空，抛出异常：java.util.NoSuchElementException
        System.out.println(blockingQueue.remove());
    }

    /**
     * take() 方法：获取并移除队列头部元素
     * - 队列为空则阻塞线程直到队列有元素
     */
    private static void testTake() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        try {
            blockingQueue.put(1);
            blockingQueue.put(2);
            System.out.println(blockingQueue.take());
            System.out.println(blockingQueue.take());
            // 队列为空，阻塞等待
            System.out.println(blockingQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * peek() 方法：获取队列头部元素但不移除
     * - 队列为空返回 null
     */
    private static void testPeek() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(2);
        // 始终返回队列头部元素
        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.peek());
        // 队列为空，返回 null
        blockingQueue.clear();
        System.out.println(blockingQueue.peek());
    }

    /**
     * element() 方法：获取队列头部元素但不移除
     * - 队列为空抛出异常
     */
    private static void testElement() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(2);
        // 始终返回队列头部元素
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());
        // 队列为空，抛出异常：java.util.NoSuchElementException
        blockingQueue.clear();
        System.out.println(blockingQueue.element());
    }

}
