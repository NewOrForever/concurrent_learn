package org.example.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ClassName:CompletionServiceDemo2
 * Package:future
 * Description: CompletionService 使用案例：
 * 实现类似 Dubbo 的 Forking Cluster场景 - 通过并行调用多个查询服务，只要有一个成功返回结果，整个服务就可以返回了
 *
 * @Date:2024/9/30 11:09
 * @Author:qs@1.com
 */
public class CompletionServiceDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("服务返回结果：" + forkingClusterQuery());
    }

    private static Integer forkingClusterQuery() throws InterruptedException, ExecutionException {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 用于保存Future对象
        List<Future<Integer>> futures = new ArrayList<>(3);
        // 提交异步任务，并保存future到futures
        futures.add(cs.submit(CompletionServiceDemo2::geocoderByS1));
        futures.add(cs.submit(CompletionServiceDemo2::geocoderByS2));
        futures.add(cs.submit(CompletionServiceDemo2::geocoderByS3));
        // 获取最快返回的任务执行结果
        Integer r = 0;
        try {
            // 只要有一个成功返回，则break
            for (int i = 0; i < 3; ++i) {
                r = cs.take().get();
                // 简单地通过判空来检查是否成功返回
                if (r != null) {
                    break;
                }
            }
        } finally {
            /**
             * 获取最快返回的任务结果后需要 -> 取消所有任务
             */
            for (Future<Integer> f : futures)
                f.cancel(true);
        }
        // 返回结果
        return r;
    }

    private static Integer geocoderByS1() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5000);
        System.out.println("S1:查询地理位置...");
        return 1;
    }

    private static Integer geocoderByS2() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(8000);
        System.out.println("S2:查询地理位置...");
        return 2;
    }

    private static Integer geocoderByS3() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(3000);
        System.out.println("S3:查询地理位置...");
        return 3;
    }

}
