package org.example.sync.jvmoptimization;

/**
 * ClassName:TestLockCoarse
 * Package:org.example.sync.jvmoptimization
 * Description: 测试锁粗化
 * 如何关闭锁粗化：
 * -XX:-DoEscapeAnalysis 关闭逃逸分析
 *
 * @Date:2024/8/22 17:28
 * @Author:qs@1.com
 */
public class TestLockCoarse {
    /**
     * 锁消除和锁粗化这两个测试类的区别：
     * - 当前锁粗化测试类中 {@link TestLockCoarse#stringBuffer} 这里是全局变量，会被多个线程访问，所以不会消除锁
     * - {@link TestLockEliminate} 中的 stringBuffer 是局部变量，不会被其他线程访问，所以可以消除锁
     */
    StringBuffer stringBuffer = new StringBuffer();

    public void append(String str1, String str2) {
        /**
         * stringBuffer.append(str1).append(str2) 这两个操作是连续的，jvm 会将这两个操作合并为一个操作，这样就减少了加锁的次数
         * 最终只需要在第一次 append 时加锁，最后一次 append 结束后解锁
         */
        stringBuffer.append(str1).append(str2);
    }

    public static void main(String[] args) throws InterruptedException {
        TestLockCoarse demo = new TestLockCoarse();
        /**
         * 测试锁粗化的性能 -> 用于和锁消除{@link TestLockEliminate}的性能对比
         * 锁消除 > 锁粗化 > 关闭锁消除
         *
         * 为什么锁消除的性能要比锁粗化的性能高？
         * 因为锁消除是直接消除了锁，而锁粗化是减少了加锁的次数，但是并没有消除锁
         */
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            demo.append("aaa", "bbb");
        }
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - start) + " ms");
    }

}
