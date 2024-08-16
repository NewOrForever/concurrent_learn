package org.example.sync;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * ClassName:AtomicReferenceFieldUpdaterTest
 * Package:org.example.cas
 * Description:
 *
 * @Date:2024/8/14 15:05
 * @Author:qs@1.com
 */
public class AtomicReferenceFieldUpdaterTest {
    private static final AtomicReferenceFieldUpdater<ReferenceCandidate, Integer> scoreUpdater =
            AtomicReferenceFieldUpdater.newUpdater(ReferenceCandidate.class, Integer.class, "score");
    private static AtomicInteger realScore = new AtomicInteger();

    public static void main(String[] args) {
        final ReferenceCandidate candidate = new ReferenceCandidate();

        Thread[] threadArr = new Thread[10000];
        for (int i = 0; i < threadArr.length; i++) {
            threadArr[i] = new Thread(() -> {
                if (Math.random() > 0.4) {
                    // 旧值 + 1 -> 累加
                    scoreUpdater.accumulateAndGet(candidate, 1, Integer::sum);
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

        System.out.println("AtomicReferenceFieldUpdater score: " + candidate.score);
        System.out.println("AtomicInteger score: " + candidate.score2);
        System.out.println("realScore: " + realScore);

        System.out.println("------------------- 测试 compareAndSet ---------------------");
        scoreUpdater.set(candidate, 0);
        boolean success = scoreUpdater.compareAndSet(candidate, 0, 100);
        System.out.println("AtomicReferenceFieldUpdater compareAndSet: " + success + ", score: " + candidate.score);
    }

}

class ReferenceCandidate {
    volatile Integer score = 0;
    AtomicInteger score2 = new AtomicInteger();
}

