package future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * ClassName:CompletableFutureDemo02
 * Package:future
 * Description: 测试 CompletableFuture 的结果处理
 * whenComplete(BiConsumer<? super T, ? super Throwable> action)：当CompletableFuture的计算结果完成，或者抛出异常的时候，执行提供的 action
 * whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action)：异步执行 whenComplete
 * whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor)：自定义线程池异步执行 whenComplete
 * exceptionally(Function<Throwable, ? extends T> fn)：当CompletableFuture的计算结果完成，但是抛出异常的时候，执行提供的 fn
 *
 * @Date:2024/9/30 13:50
 * @Author:qs@1.com
 */
public class CompletableFutureDemo02 {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if (new Random().nextBoolean()) {
                int i = 12 / 0;
            }
            System.out.println("执行结束！");
            return "test";
        });

        // whenComplete
        future.whenComplete((result, throwable) -> {
            if (throwable == null) {
                System.out.println(Thread.currentThread().getName() + "：whenComplete 处理 -> 任务正常完成，结果：" + result);
            } else {
                System.out.println(Thread.currentThread().getName() + "：whenComplete 处理 -> 任务异常，" + "任务结果：" + result + "，异常信息：" + throwable.getMessage());
            }
        });

        // whenCompleteAsync
        future.whenCompleteAsync((result, throwable) -> {
            if (throwable == null) {
                System.out.println(Thread.currentThread().getName() + "：whenCompleteAsync 处理 -> 任务正常完成，结果：" + result);
            } else {
                System.out.println(Thread.currentThread().getName() + "：whenCompleteAsync 处理 -> 任务异常，异常信息：" + throwable.getMessage());
            }
        });

        // exceptionally
        future.exceptionally(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) {
                System.out.println("当前线程" + Thread.currentThread().getName() + "exceptionally 处理异常结果：" + throwable.getMessage());
                return "异常xxxx处理完成";
            }
        }).join();
    }

}
