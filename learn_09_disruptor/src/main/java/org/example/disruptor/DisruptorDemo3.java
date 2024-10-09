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
 * ClassName:DisruptorDemo3
 * Package:org.example.disruptor
 * Description: Disruptor 使用
 * 消费者优先级模式
 *
 * @Date:2024/10/9 14:13
 * @Author:qs@1.com
 */
public class DisruptorDemo3 {
    public static void main(String[] args) {
        // 创建 Disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                new OrderEventFactory(),
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI, // 多生产者
                new YieldingWaitStrategy()); // 等待策略


        // 设置消费者用于处理 RingBuffer 的事件（单消费者）
        // disruptor.handleEventsWith(new OrderEventHandler());
        // 多消费者(会重复消费消息 - 类似消息队列的广播模式)
        // disruptor.handleEventsWith(new OrderEventHandler(), new OrderEventHandler());
        // 多消费者(不会重复消费消息 - 类似消息队列的集群模式)
        // disruptor.handleEventsWithWorkerPool(new OrderEventHandler(), new OrderEventHandler());
        // 按消费者优先级消费：消费者A -> (消费者B 消费者C 集群消费) -> 消费者D
        /**
         * 执行结果：
         * 1. 有序消费：消费者A -> 消费者B/消费者C -> 消费者D
         * 2. 消费者A和D分别能消费到所有消息，消费者B和C共同消费所有消息
         */
        disruptor.handleEventsWith(new OrderEventHandler())
                .thenHandleEventsWithWorkerPool(new OrderEventHandler(), new OrderEventHandler())
                .then(new OrderEventHandler());

        // 启动 Disruptor
        disruptor.start();

        //  获取RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        new Thread(() -> {
            OrderEventProducer producer = new OrderEventProducer(ringBuffer);
            for (int i = 0; i < 10; i++) {
                producer.onData(i, "Hello" + i);
            }
        }, "producer01").start();

        // 关闭 Disruptor
        // disruptor.shutdown();
    }

}
