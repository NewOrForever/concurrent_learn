package org.example.sync.biaslock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * ClassName:EndianExample
 * Package:org.example.sync.biaslock
 * Description: 测试大端小端存储
 * 大端存储：高位字节存放在低内存地址 -> 高位字节在前，低位字节在后
 * 小端存储：高位字节存放在高内存地址 -> 低位字节在前，高位字节在后
 *
 * @Date:2024/8/17 14:09
 * @Author:qs@1.com
 */
public class EndianExample {
    public static void main(String[] args) {
        int a = 0x12345678;
        // 小端存储
        ByteBuffer littleEndianBuffer = ByteBuffer.allocate(4);
        littleEndianBuffer.order(ByteOrder.LITTLE_ENDIAN);
        littleEndianBuffer.putInt(a);
        byte[] littleEndianBytes = littleEndianBuffer.array();
        System.out.println("小端存储：");
        for (byte b : littleEndianBytes) {
            System.out.printf("0x%02X  ", b);
        }

        System.out.println();

        // 大端存储
        ByteBuffer bigEndianBuffer = ByteBuffer.allocate(4);
        bigEndianBuffer.order(ByteOrder.BIG_ENDIAN);
        bigEndianBuffer.putInt(a);
        byte[] bigEndianBytes = bigEndianBuffer.array();
        System.out.println("大端存储：");
        for (byte b : bigEndianBytes) {
            System.out.printf("0x%02X  ", b);
        }
    }

}
