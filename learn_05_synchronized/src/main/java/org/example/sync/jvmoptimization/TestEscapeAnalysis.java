package org.example.sync.jvmoptimization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:TestEscapeAnalysis
 * Package:org.example.sync.jvmoptimization
 * Description: 测试逃逸分析
 * 对象如果没有发生逃逸，就能在 JIT 编译时进行优化，将对象分配在栈/寄存器中，而不是堆中
 *
 * @Date:2024/8/23 11:17
 * @Author:qs@1.com
 */
@Slf4j
public class TestEscapeAnalysis {
    /**
     * 进行两种测试
     * 关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
     * VM运行参数：-Xmx4G -Xms4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 开启逃逸分析  jdk8默认开启
     * VM运行参数：-Xmx4G -Xms4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 执行main方法后
     * jps 查看进程
     * jmap -histo 进程ID
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
             alloc();
            // test2();
        }

        long end = System.currentTimeMillis();

        log.info("执行时间：" + (end - start) + " ms");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * JIT编译时会对代码进行逃逸分析
     * 并不是所有对象存放在堆区，有的一部分存在线程栈空间
     * Ponit没有逃逸
     * jmap -histo 进程ID 查看堆内存中 Point 对象的数量：#instances
     *  - 开启逃逸分析：Point对象数量小于循环创建的对象数量 500000
     *  - 关闭逃逸分析：Point对象数量等于循环创建的对象数量 500000
     */
    private static String alloc() {
        Point point = new Point();
        /**
         * 如果释放这个方法调用，就会导致 Point 对象方法逃逸 -> 就不会进行堆分配转化成栈分配的优化
         */
        // testMethodEscape(point);
        return point.toString();
    }

    /**
     * 同步省略（锁消除）  JIT编译阶段优化，JIT经过逃逸分析之后发现无线程安全问题，就会做锁消除
     */
    public void append(String str1, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str1).append(str2);
    }

    /**
     * 标量替换
     */
    private static void test2() {
        Point point = new Point(1, 2);
        System.out.println("point.x=" + point.getX() + "; point.y=" + point.getY());

        /**
         * 如果释放这个方法调用，就会导致 Point 对象方法逃逸 -> 就不会进行标量替换的优化
         */
        // testMethodEscape(point);
//        int x=1;
//        int y=2;
//        System.out.println("point.x="+x+"; point.y="+y);
    }

    static void testMethodEscape(Point point) {

    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Point {
    private int x;
    private int y;
}
