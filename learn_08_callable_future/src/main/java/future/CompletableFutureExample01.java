package future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CompletableFutureExample01
 * Package:future
 * Description: CompletableFuture 使用案例
 * 烧水泡茶
 * 1. T1 洗水壶 -> 烧开水 -> 泡茶（获取T2任务的茶叶）
 * 2. T2 洗茶壶 -> 洗茶杯 -> 拿茶叶
 * 3. CompletableFuture 实现
 *
 * @Date:2024/10/8 9:31
 * @Author:qs@1.com
 */
public class CompletableFutureExample01 {
    public static void main(String[] args) {
        // T1 任务：洗水壶 -> 烧开水
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1:洗水壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1:烧开水...");
            sleep(15, TimeUnit.SECONDS);
        });

        // T2 任务：洗茶壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T2:洗茶壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T2:洗茶杯...");
            sleep(2, TimeUnit.SECONDS);

            System.out.println("T2:拿茶叶...");
            sleep(1, TimeUnit.SECONDS);
            return "龙井";
        });

        // T3 任务：任务 1 和任务 2 完成后执行泡茶
        future1.thenCombine(future2, (resultFuture1_NOP, resultFuture2) -> {
            System.out.println("T1:拿到茶叶：" + resultFuture2);
            System.out.println("T1:泡茶...");
            return "上茶：" + resultFuture2;
        }).thenAccept(System.out::println).join();
    }

    private static void sleep(int t, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
