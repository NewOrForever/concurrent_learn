package org.example.cas;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * ClassName:AtomicIntegerFieldUpdaterTest
 * Package:org.example.cas
 * Description: java 原子整型更新器测试
 * AtomicIntegerFieldUpdater 保证对象中的属性的原子性, 但是不能保证对象的原子性
 * 字段要求：
 * 1. 字段必须是 volatile 修饰的
 * 2. 字段必须是非 static 修饰的
 * 3. 字段的类型必须是 int
 * 4. 只能修改可见范围内的字段
 * 5. 不能修改 final 字段
 * 6. 不能修改父类字段
 *
 * @Date:2024/8/14 11:21
 * @Author:qs@1.com
 */
public class AtomicIntegerFieldUpdaterTest {

    public static void main(String[] args) {
        test();
        System.out.println("----------------------------------------");
        test(Candidate.class, "score", "score2");
        System.out.println("------------------- 测试修改父类字段 ---------------------");
        test(SubCandidate.class, "score", "score2");
    }

    private static void test() {
        final AtomicInteger realScore = new AtomicInteger();

        final AtomicIntegerFieldUpdater<Candidate> scoreUpdater =
                AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

        final Candidate candidate = new Candidate();

        Thread[] threadArr = new Thread[10000];
        for (int i = 0; i < threadArr.length; i++) {
            threadArr[i] = new Thread(() -> {
                if (Math.random() > 0.4) {
                    scoreUpdater.incrementAndGet(candidate);
                    candidate.score2.incrementAndGet();
                    realScore.incrementAndGet();
                }
            });
            threadArr[i].start();
        }

        for (int i = 0; i < threadArr.length; i++) {
            try {
                threadArr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("AtomicIntegerFieldUpdater score: " + candidate.score);
        System.out.println("AtomicInteger score: " + candidate.score2);
        System.out.println("realScore: " + realScore);
    }

    private static <T> void test(Class<T> tClass, String fieldName, String score2FieldName) {
        final AtomicInteger realScore = new AtomicInteger();

        final AtomicIntegerFieldUpdater<T> scoreUpdater =
                AtomicIntegerFieldUpdater.newUpdater(tClass, fieldName);

        final T candidate;
        try {
            candidate = tClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Thread[] threadArr = new Thread[10000];
        for (int i = 0; i < threadArr.length; i++) {
            threadArr[i] = new Thread(() -> {
                if (Math.random() > 0.4) {
                    scoreUpdater.incrementAndGet(candidate);
                    realScore.incrementAndGet();
                    if (score2FieldName != null) {
                        try {
                            AtomicInteger score2 = (AtomicInteger) tClass.getDeclaredField(score2FieldName).get(candidate);
                            score2.incrementAndGet();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            threadArr[i].start();
        }

        for (int i = 0; i < threadArr.length; i++) {
            try {
                threadArr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 获取candidate对象中的score字段的值
        int score = 0;
        AtomicInteger score2 = null;
        try {
            Field[] fields = candidate.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (fieldName.equals(field.getName())) {
                    field.setAccessible(true);
                    score = (int) field.get(candidate);
                } else if (score2FieldName != null && score2FieldName.equals(field.getName())) {
                    field.setAccessible(true);
                    score2 = (AtomicInteger) field.get(candidate);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        System.out.println("AtomicIntegerFieldUpdater score: " + score);
        System.out.println("AtomicInteger score: " + score2);
        System.out.println("realScore: " + realScore);
    }
}

class Candidate {
    volatile int score = 0;
    AtomicInteger score2 = new AtomicInteger();
}

class SubCandidate extends Candidate {

}
