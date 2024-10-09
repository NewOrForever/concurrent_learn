package org.example.disruptor.producer;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import org.example.disruptor.event.OrderEvent;

/**
 * ClassName:OrderEventProducer
 * Package:org.example.disruptor.producer
 * Description: 生产者
 *
 * @Date:2024/10/8 13:17
 * @Author:qs@1.com
 */
public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long value, String name) {
        // 申请写入一个元素 - 获取下一个可用的序号
        long sequence = ringBuffer.next();
        try {
            // 用序号获取空闲事件
            OrderEvent orderEvent = ringBuffer.get(sequence);
            /**
             * 为什么这里不需要判断 orderEvent 是否为空？
             * 1. 因为 Disruptor 会在初始化时，就创建好 RingBuffer，并且初始化 RingBuffer 时，会创建好所有的事件对象
             * 2. 所以这里不需要判断 orderEvent 是否为空，因为 orderEvent 一定不为空
             * @see com.lmax.disruptor.RingBufferFields#fill(EventFactory)
             */
            // 写入事件数据
            orderEvent.setValue(value);
            orderEvent.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("生产者发送数据 value: " + value + " ,name: " + name);
            // 发布事件
            // 多生产模式下会设置 available Buffer 里面相应的位置标记是否写入成功
            ringBuffer.publish(sequence);
        }
    }

}
