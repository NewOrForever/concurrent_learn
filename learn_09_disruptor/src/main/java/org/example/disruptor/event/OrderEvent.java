package org.example.disruptor.event;

import lombok.Data;

/**
 * ClassName:OrderEvent
 * Package:org.example.disruptor.event
 * Description: 事件
 *
 * @Date:2024/10/8 13:13
 * @Author:qs@1.com
 */
@Data
public class OrderEvent {
    private long value;
    private String name;
}
