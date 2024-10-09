package org.example.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * ClassName:OrderEventFactory
 * Package:org.example.disruptor.event
 * Description: 事件工厂
 *
 * @Date:2024/10/8 13:13
 * @Author:qs@1.com
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
