package org.example.communication;

import java.io.*;

/**
 * ClassName:PipedTest
 * Package:org.example.common_method.communication
 * Description: java 线程间通信 - 管道通信
 * 1. 管道通信是通过管道流实现的，一个线程向管道流中写入数据，另一个线程从管道流中读取数据
 * 2. 管道通信是同步的，一个线程写入数据时，另一个线程必须读取数据，否则会阻塞
 * 3. 管道通信可以用于线程间通信，但是不推荐使用，因为管道流的容量有限，容易造成线程阻塞
 *
 * @Date:2024/8/12 16:33
 * @Author:qs@1.com
 */
public class PipedTest {
    public static void main(String[] args) throws IOException {
        // testPipedDemo();
        testPiped();
    }

    private static void testPiped() throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        // 将输出流和输入流进行连接，否则在使用时会抛出IOException
        out.connect(in);

        Thread printThread = new Thread(new PrintRunnable(in));
        printThread.start();

        int receivedData;
        try {
            // 从控制台读取数据，写入管道流
            while ((receivedData = System.in.read()) != -1) {
                out.write(receivedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    private static void testPipedDemo() {
        // 创建管道输入输出流
        PipedReader in = new PipedReader();
        PipedWriter out = new PipedWriter();

        // 将输入输出流连接
        try {
            out.connect(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 创建线程向管道流中写入数据
        Thread t1 = new Thread(() -> {
            try {
                out.write("hello, piped writer");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // 创建线程从管道流中读取数据
        Thread t2 = new Thread(() -> {
            int data;
            try {
                while ((data = in.read()) != -1) {
                    System.out.println((char) data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }
}

class PrintRunnable implements Runnable {
    private PipedReader in;

    public PrintRunnable(PipedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        int receivedData;
        try {
            /**
             * 从管道流中读取数据，输出到控制台
             * @see PipedReader#in 初始没有数据时，值为 -1，会阻塞
             * 当管道有数据时，{@link PipedReader#in} 值大于0，如果 {@link PipedReader#out} 与 {@link PipedReader#in} 不相等
             * 则返回数据否则阻塞（{@link PipedReader#in} 被充值为 -1）
              */
            while ((receivedData = in.read()) != -1) {
                System.out.println((char) receivedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
