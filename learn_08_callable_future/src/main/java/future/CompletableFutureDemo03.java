package future;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ClassName:CompletableFutureDemo03
 * Package:future
 * Description: 测试 CompletableFuture 的一些方法
 * <p>
 * 结果转换：
 * thenApply(Function<? super T,? extends U> fn)：当CompletableFuture的计算结果完成，执行提供的 fn，返回一个新的CompletableFuture
 * thenApplyAsync(Function<? super T,? extends U> fn)：异步执行 thenApply
 * thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)：自定义线程池异步执行 thenApply
 * >>>>> 上一个CompletableFuture的结果作为下一个任务的入参
 * <p>
 * thenCompose(Function<? super T,? extends CompletionStage<U>> fn)：当CompletableFuture的计算结果完成，执行提供的 fn，返回一个新的CompletableFuture
 * thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn)：异步执行 thenCompose
 * thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn, Executor executor)：自定义线程池异步执行 thenCompose
 * >>>>> 用来连接两个有依赖关系的任务，结果由第二个任务返回
 * <p>
 * 结果消费：
 * thenAccept(Consumer<? super T> action)：当CompletableFuture的计算结果完成，执行提供的 action
 * thenAcceptAsync(Consumer<? super T> action)：异步执行 thenAccept
 * thenAcceptAsync(Consumer<? super T> action, Executor executor)：自定义线程池异步执行 thenAccept
 * >>>>> 对单个结果进行消费，无返回值
 * thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)：当这个CompletableFuture和other都正常完成计算的时候，执行提供的 action
 * thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)：异步执行 thenAcceptBoth
 * thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action, Executor executor)：自定义线程池异步执行 thenAcceptBoth
 * >>>>> 对两个结果进行消费，无返回值
 * <p>
 * thenRun(Runnable action)：当CompletableFuture的计算结果完成，执行提供的 action
 * thenRunAsync(Runnable action)：异步执行 thenRun
 * thenRunAsync(Runnable action, Executor executor)：自定义线程池异步执行 thenRun
 * >>>>> 对结果不关心，执行下一个操作
 * <p>
 * 结果组合：
 * thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)：当这个CompletableFuture和other都正常完成计算的时候，执行提供的 fn，返回一个新的CompletableFuture
 * thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)：异步执行 thenCombine
 * thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn, Executor executor)：自定义线程池异步执行 thenCombine
 * >>>>> 用来组合两个独立的CompletableFuture，结果由第一个任务返回
 * <p>
 * 任务交互：
 * applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn)：当这个CompletableFuture和other中有一个正常完成计算的时候，执行提供的 fn，返回一个新的CompletableFuture
 * apptoEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn)：异步执行 applyToEither
 * apptoEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor)：自定义线程池异步执行 applyToEither
 * >>>>> 两个线程任务相比较，先获得执行结果的，就对该结果进行下一步操作 -> 有返回值
 * <p>
 * acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action)：当这个CompletableFuture和other中有一个正常完成计算的时候，执行提供的 action
 * acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action)：异步执行 acceptEither
 * acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor)：自定义线程池异步执行 acceptEither
 * >>>>> 两个线程任务相比较，先获得执行结果的，就对该结果进行消费 -> 无返回值
 * <p>
 * runAfterEither(CompletionStage<?> other, Runnable action)：当这个CompletableFuture和other中有一个正常完成计算的时候，执行提供的 action
 * runAfterEitherAsync(CompletionStage<?> other, Runnable action)：异步执行 runAfterEither
 * runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor)：自定义线程池异步执行 runAfterEither
 * >>>>> 两个线程任务相比较，有任何一个执行完成，就进行下一步操作，不关心运行结果 -> 无返回值
 * <p>
 * runAfterBoth(CompletionStage<?> other, Runnable action)：当这个CompletableFuture和other都正常完成计算的时候，执行提供的 action
 * runAfterBothAsync(CompletionStage<?> other, Runnable action)：异步执行 runAfterBoth
 * runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor)：自定义线程池异步执行 runAfterBoth
 * >>>>> 两个线程任务都执行完成，才进行下一步操作，不关心运行结果 -> 无返回值
 * <p>
 * anyOf(CompletableFuture<?>... cfs)：任意一个CompletableFuture完成，就执行下一步操作
 * >>>>> 任意一个线程任务执行完成，就对该结果进行下一步操作 -> 返回这个 CompletableFuture
 * allOf(CompletableFuture<?>... cfs)：所有CompletableFuture完成，才执行下一步操作
 * >>>>> 所有线程任务执行完成，才进行下一步操作
 *
 * @Date:2024/9/30 13:50
 * @Author:qs@1.com
 */
public class CompletableFutureDemo03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // testThenApply();
        // testThenCompose();
        // testAccept();
        // testThenAcceptBoth();
        // testThenRun();
        // testThenCombine();
        // testApplyToEither();
        // testAcceptEither();
        // testRunAfterEither();
        // testRunAfterBoth();
        // testAnyOf();
        testAllOf();
    }

    private static void testAllOf() {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        int number = new Random().nextInt(5);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("future1 完成");
                        return "hello";
                    }
                });
        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        int number = new Random().nextInt(3);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("future2 完成");
                        return "world";
                    }
                });
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future1, future2);
        try {
            allOfFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("future1 is done: " + future1.isDone() + " - 结果：" + future1.join() + "，future2 is done: " + future2.isDone() + " - 结果：" + future2.join());
    }

    private static void testAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "hello";
                    }
                });
        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "world";
                    }
                });
        CompletableFuture<Object> future = CompletableFuture.anyOf(future1, future2);
        System.out.println("最终结果：" + future.get());
    }

    private static void testRunAfterBoth() {
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务A：" + number);
                        return number;
                    }
                });
        CompletableFuture<Integer> future2 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务B：" + number);
                        return number;
                    }
                });
        future1.runAfterBoth(future2, new Runnable() {
            @Override
            public void run() {
                System.out.println("两个任务都执行完成了");
            }
        }).join();
    }

    private static void testRunAfterEither() {
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务A：" + number);
                        return number;
                    }
                });
        CompletableFuture<Integer> future2 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务B：" + number);
                        return number;
                    }
                });
        future1.runAfterEither(future2, new Runnable() {
            @Override
            public void run() {
                System.out.println("已经有一个任务执行完成");
            }
        }).join();
    }

    private static void testAcceptEither() {
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        System.out.println("任务A ---> start：" + number);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务A ---> end：" + number);
                        return number;
                    }
                });
        CompletableFuture<Integer> future2 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        System.out.println("任务B >>> start：" + number);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务B >>> end：" + number);
                        return number;
                    }
                });
        future1.acceptEither(future2, new Consumer<Integer>() {
            @Override
            public void accept(Integer fasterResult) {
                System.out.println("最快结果：" + fasterResult);
            }
        }).join();
    }

    private static void testApplyToEither() {
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        System.out.println("任务A ---> start：" + number);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务A ---> end：" + number);
                        return number;
                    }
                });
        CompletableFuture<Integer> future2 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int number = new Random().nextInt(10);
                        System.out.println("任务B >>> start：" + number);
                        try {
                            TimeUnit.SECONDS.sleep(number);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("任务B >>> end：" + number);
                        return number;
                    }
                });
        Integer finalResult = future1.applyToEither(future2, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer fasterResult) {
                System.out.println("最快结果：" + fasterResult);
                return fasterResult * 10;
            }
        }).join();
        System.out.println("最终结果：" + finalResult);
    }

    private static void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(5) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始值A：" + number);
            return number;
        });

        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(5) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始值B：" + number);
            return number;
        });

        CompletableFuture<Integer> future = futureA.thenCombine(futureB, (resultA, resultB) -> {
            return resultA + resultB;
        });
        System.out.println("最终结果：" + future.get());
    }

    private static void testThenRun() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            int result = 100;
            System.out.println(Thread.currentThread().getName() + "初始值：" + result);
            return result;
        }).thenRun(() -> {
            System.out.println(Thread.currentThread().getName() + "执行thenRun");
        });
        System.out.println("最终结果：" + future.join());
    }

    private static void testThenAcceptBoth() {
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(3) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始值A：" + number);
            return number;
        });

        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(3) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始值B：" + number);
            return number;
        });

        futureA.thenAcceptBoth(futureB, (resultA, resultB) -> {
            int result = resultA + resultB;
            System.out.println("最终结果：" + result);
        }).join();
    }

    private static void testAccept() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            int result = 100;
            System.out.println(Thread.currentThread().getName() + "初始值：" + result);
            return result;
        }).thenAccept(result -> {
            int newResult = result * 5;
            System.out.println(Thread.currentThread().getName() + "二次计算结果：" + newResult);
        });
        System.out.println("最终结果：" + future.join());
    }

    private static void testThenCompose() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int result = 100;
            System.out.println(Thread.currentThread().getName() + "初始值：" + result);
            return result;
        }).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer prevResult) {
                System.out.println(Thread.currentThread().getName() + "线程还未执行 supplyAsync 方法");
                return CompletableFuture.supplyAsync(() -> {
                    int result = prevResult * 5;
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + "二次计算结果：" + result);
                    return result;
                });
            }
        }).thenComposeAsync(preResult -> CompletableFuture.supplyAsync(() -> {
            int result = preResult * 2;
            System.out.println(Thread.currentThread().getName() + "三次计算结果：" + result);
            return result;
        }, Executors.newSingleThreadExecutor()));
        System.out.println("最终结果：" + future.join());
    }

    private static void testThenApply() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int result = 100;
            System.out.println(Thread.currentThread().getName() + "初始值：" + result);
            return result;
        }).thenApply(preResult -> {
            int result = preResult * 5;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "二次计算结果：" + result);
            return result;
        }).thenApplyAsync(preResult -> {
            int result = preResult * 2;
            System.out.println(Thread.currentThread().getName() + "三次计算结果：" + result);
            return result;
        }, Executors.newSingleThreadExecutor());
        System.out.println("最终结果：" + future.get());
    }

}
