package org.example.sync.jvmoptimization;

/**
 * ClassName:TestLockEliminate
 * Package:org.example.sync.jvmoptimization
 * Description: 测试锁消除
 *
 * @Date:2024/8/22 17:28
 * @Author:qs@1.com
 */
public class TestLockEliminate {
    /**
     * 锁消除
     * -XX:+EliminateLocks 开启锁消除(jdk8默认开启）
     * -XX:-EliminateLocks 关闭锁消除
     *
     * @param str1
     * @param str2
     */
    public void append(String str1, String str2) {
        /**
         * StringBuffer 是线程安全的，但是在这里是局部变量，不会被其他线程访问，所以可以消除锁
         */
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str1).append(str2);
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 锁消除
         * -XX:+EliminateLocks 开启锁消除(jdk8默认开启）
         * -XX:-EliminateLocks 关闭锁消除
         * 比较开启锁消除和关闭锁消除的性能差异 -> 开启锁消除性能要比关闭锁消除性能高很多的
         */
        TestLockEliminate demo = new TestLockEliminate();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            demo.append("aaa", "bbb");
        }
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - start) + " ms");
    }

}
