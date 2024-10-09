package org.example.disruptor.consumer;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.example.disruptor.event.OrderEvent;

/**
 * ClassName:OrderEventHandler
 * Package:org.example.disruptor.consumer
 * Description: 消费者
 *
 * @Date:2024/10/9 11:04
 * @Author:qs@1.com
 */
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("消费者" + Thread.currentThread().getName() +
                "消费数据 value: " + event.getValue() + " ,name: " + event.getName() + " ,sequence: " + sequence + " ,endOfBatch: " + endOfBatch);
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        System.out.println("消费者" + Thread.currentThread().getName() +
                "消费数据 value: " + event.getValue() + " ,name: " + event.getName());
    }

}
