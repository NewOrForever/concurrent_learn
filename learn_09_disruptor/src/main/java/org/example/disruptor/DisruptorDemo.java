package org.example.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.*;
import org.example.disruptor.consumer.OrderEventHandler;
import org.example.disruptor.event.OrderEvent;
import org.example.disruptor.event.OrderEventFactory;
import org.example.disruptor.producer.OrderEventProducer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ClassName:DisruptorDemo
 * Package:org.example.disruptor
 * Description: Disruptor 使用
 *
 * @Date:2024/10/9 11:05
 * @Author:qs@1.com
 */
public class DisruptorDemo {
    public static void main(String[] args) {
        // 创建 Disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                new OrderEventFactory(),
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE, // 单生产者
                new YieldingWaitStrategy()); // 等待策略

        /**
         * 消费者消费源码流程：
         * @see Disruptor#start() 先启动 Disruptor
         * @see ConsumerInfo#start(Executor) 线程池启动消费者
        *      - handleEventsWith 方法进入的是 {@link EventProcessorInfo} -> 最终执行 {@link BatchEventProcessor} 这个任务
         *          - {@link Disruptor#createEventProcessors(Sequence[], EventHandler[]) -> {@link ConsumerRepository#add(EventProcessor, EventHandler, SequenceBarrier)
         *     - handleEventsWithWorkerPool 方法进入的是 {@link WorkerPoolInfo} -> 最终执行 {@link WorkProcessor} 这个任务
         *          - {@link Disruptor#createWorkerPool(Sequence[], WorkHandler[])} -> {@link ConsumerRepository#add(WorkerPool, SequenceBarrier)}
         *          - 通过 {@link WorkProcessor#workSequence}属性来保证不会重复消费消息
         *              - {@link WorkProcessor#sequence} 保存的是当前消费者消费到的位置
         *              - {@link WorkProcessor#workSequence} 保存的是{@link WorkProcessor}中所有消费者消费到的位置
         *              - 通过 CAS {@link WorkProcessor#workSequence}来保证不会重复消费消息
         */
        // 设置消费者用于处理 RingBuffer 的事件（单消费者）
        // disruptor.handleEventsWith(new OrderEventHandler());
        // 多消费者(会重复消费消息 - 类似消息队列的广播模式)
        // disruptor.handleEventsWith(new OrderEventHandler(), new OrderEventHandler());
        // 多消费者(不会重复消费消息 - 类似消息队列的集群模式)
        disruptor.handleEventsWithWorkerPool(new OrderEventHandler(), new OrderEventHandler());

        // 启动 Disruptor
        disruptor.start();

        //  获取RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        // 创建生产者
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        // 生产数据
        for (int i = 0; i < 100; i++) {
            producer.onData(i, "MSG" + i);
        }

        // 关闭 Disruptor
        disruptor.shutdown();
    }

}
