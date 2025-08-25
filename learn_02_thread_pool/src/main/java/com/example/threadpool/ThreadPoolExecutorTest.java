package com.example.threadpool;

import java.util.concurrent.*;

/**
 * ClassName:ThreadPoolExecutorTest
 * Package:com.example.thread
 * Description: 线程池源码分析
 * 线程池的核心参数：核心线程数、最大线程数、线程存活时间、任务队列、拒绝策略
 * 线程池的工作流程：提交任务 -> 判断核心线程是否满 -> 满了就放入任务队列 -> 任务队列满了就创建新线程 -> 新线程满了就执行拒绝策略
 * 线程池的拒绝策略：AbortPolicy、CallerRunsPolicy、DiscardPolicy、DiscardOldestPolicy
 * 线程池的任务队列：SynchronousQueue、LinkedBlockingQueue、ArrayBlockingQueue、PriorityBlockingQueue
 *
 * @Date:2024/2/21 16:21
 * @Author:qs@1.com
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        int COUNT_BITS = Integer.SIZE - 3;
        //  -536870912
        int RUNNING = -1 << COUNT_BITS;
        int SHUTDOWN = 0 << COUNT_BITS;
        // 1 << 29 = 00100000 00000000 00000000 00000000 = 536870912
        int STOP = 1 << COUNT_BITS;
        int TIDYING = 2 << COUNT_BITS;
        int TERMINATED = 3 << COUNT_BITS;
        System.out.println("RUNNING is :" + RUNNING);
        System.out.println("SHUTDOWN is :" + SHUTDOWN);
        System.out.println("STOP is :" + STOP);
        System.out.println("TIDYING is :" + TIDYING);
        System.out.println("TERMINATED is :" + TERMINATED);

//        testDefaultThreadPool();
//        testBeforeAndAfterExecute();
        testCustomThreadPoolExecutor();
    }

    private static void testBeforeAndAfterExecute() {
        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                20,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(20),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        ) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("准备执行任务：" + r);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("任务执行完成：" + r);
            }
        };
        executorService.execute(new MyTask(100000));
    }

    /**
     * 自定义线程池
     */
    private static void testCustomThreadPoolExecutor() throws InterruptedException {
        /**
         * 自定义线程池
         * 1. 核心线程数 - 可以理解为 公司的正式员工
         *      - 即使线程空闲，线程池也会保持的线程数量。除非设置了allowCoreThreadTimeOut，否则核心线程不会因为空闲而被终止。
         *
         * 2. 最大线程数 - 线程池中最多能创建的线程，可以理解为 公司的正式员工 + 临时员工
         *   - 核心线程数满了 >>> 放入任务队列 > 任务队列满了 >>> 创建新线程 > 新线程满了 >>> 执行拒绝策略
         *   - 最大线程数的设置需要根据业务需求来定，如果是 IO 密集型的任务，可以设置大一点，如果是 CPU 密集型的任务，可以设置小一点
         *   - 如果是 8核16线程的服务器，最大线程数如何设置？
         *      - IO 密集型的任务，可以设置为 8 * 2 = 16
         *      - CPU 密集型的任务，可以设置为 8
         *      - 为什么 IO 密集型的任务可以设置为 16，因为 IO 密集型的任务，线程大部分时间都是在等待 IO，不会占用 CPU，所以可以设置大一点
         *      - 为什么 CPU 密集型的任务可以设置为 8，因为 CPU 密集型的任务，线程大部分时间都是在执行任务，会占用 CPU，所以可以设置小一点
         *
         * 3. 线程存活时间 - 临时员工的合同期限
         *      - 当线程数超过corePoolSize时，这是多余空闲线程在终止前等待新任务的最长时间。只有当线程数大于corePoolSize（或者allowCoreThreadTimeOut 设置为true）时，keepAliveTime才会起作用。
         *      - 值为0时，多余 的空闲线程会被立即终止
         *
         * 4. 时间单位 - 线程存活时间的单位
         *
         * 5. 任务队列  - 用于存放待执行任务的队列。此队列仅包含通过execute方法提交的Runnable任务。
         *    - 核心线程数满了，还有任务进来就会放入队列中
         *    - SynchronousQueue：同步队列，不存储任务，只是传递任务，如果没有线程来取任务，就会阻塞
         *    - LinkedBlockingQueue：无界队列，可以存储无限多的任务
         *    - ArrayBlockingQueue：有界队列，可以存储固定数量的任务
         *    - PriorityBlockingQueue：优先级队列
         *    - 自定义队列
         *
         * 6. 线程工厂 - 用于创建新线程的线程工厂。线程工厂通常用于定义新线程的创建方式，例如设置线程名称、优先级等。
         *      - Executors.defaultThreadFactory() 默认的线程工厂
         *      - Executors.privilegedThreadFactory() 特权线程工厂
         *      - 自定义线程工厂
         *          - 自定义线程工厂的作用：可以给线程设置名字、优先级、是否是守护线程等
         *              - 线程池的线程都是守护线程，当主线程结束后，线程池的线程也会结束
         *              - 线程池的线程都是非守护线程，当主线程结束后，线程池的线程不会结束
         *
         * 7. 拒绝策略 - 任务队列满了，线程池满了，还有任务进来，怎么处理
         *    - AbortPolicy：直接抛出异常
         *    - CallerRunsPolicy：由调用execute方法提交任务的线程来执行这个任务
         *    - DiscardPolicy：直接丢弃任务
         *    - DiscardOldestPolicy：丢弃队列中最旧的任务，然后重新尝试执行任务
         *
         *  corePooISize、maximumPoolSize、队列容量 合理设置
         *      - maximumPoolSize：服务器的线程数 或者 2 * CPU 核心数 + 1
         *      - corePoolSize：maximumPoolSize / 2
         *      - 队列容量：(corePoolSize / taskTime) * responseTime
         *          - taskTime：任务执行时间
         *
         */
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                10,
                20,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(20),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() //new MyRejectPolicy()
        );


        /**
         * 测试结果：
         * 1. 线程1-10 执行任务 1-10 且一一对应 -> 线程11-20 执行任务 31-40 且一一对应，线程1-20 执行后续任务且不再对应
         *      - 这个结果的原因？
         *      1）第3批任务为啥先执行？因为第二批任务在队列中等待，第三批任务进来就会创建新线程执行
         *      2）我们不知道哪个线程会先领取任务，所以第二批任务由谁执行是不确定的
         *
         * 2. 队列满了，最大线程数也满了，拒绝执行后抛出异常，不再执行后续任务
         *     - 这个结果的原因？AbortPolicy 直接抛出异常
         *
         * 源码分析：
         * 1. 线程池的工作流程
         * 1）若线程池中的线程数小于核心线程数，创建新的线程执行任务
         * 2）若线程池中的线程数大于等于核心线程数，将任务添加到阻塞队列中 --> 此时核心线程数 10，阻塞队列开始存放任务
         *      - 每个线程执行完第一个任务后，会从阻塞队列中领取任务
         * 3）若阻塞队列已满，创建新的线程执行任务 --> 此时核心线程数 10，阻塞队列 20，开始创建新的非核心线程
         *      - 每个线程执行完第一个任务后，会从阻塞队列中领取任务
         * 4）若线程数量达到最大线程数，此时再有新的任务进来，执行拒绝策略
         */
        for (int i = 1; i < 1000; i++) {
            executorService.execute(new MyTask(i));
        }
        // 关闭线程池，不再接受新任务，但会继续执行已提交的任务
        executorService.shutdown();
        // 等待所有任务执行完毕，最多等待1天时间
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

    /**
     * Jdk默认的几个线程池
     */
    private static void testDefaultThreadPool() throws InterruptedException {
        /**
         * 看下默认的几个线程池的源码了解它们默认设置的参数
         * cached：核心线程数为 0，最大线程数为 Integer.MAX_VALUE，线程存活时间为 60s，任务队列为 SynchronousQueue
         *      - 每个任务都会创建一个新线程，线程存活时间为 60s，如果 60s 内没有任务，线程就会被回收
         *
         * fixed：自定义核心线程数，核心线程数等于最大线程数，线程存活时间为 0，任务队列为 LinkedBlockingQueue
         *      - keepAliveTime 为 0，任何非核心线程（在这个例子中实际上是不存在的，因为corePoolSize和maximumPoolSize相等）都不会因为空闲而被回收，直到它们被显式地关闭或系统资源耗尽
         *      - 比如我设置了 10 个核心线程，10个线程都在执行任务，新任务进来就会放入任务队列，知道有线程空闲出来就取队列中的任务执行
         * single：核心线程数为 1，核心线程数等于最大线程数，线程存活时间为 0，任务队列为 LinkedBlockingQueue
         *      - 和 fixed 线程池的区别就是核心线程数为 1，一次只能执行一个任务
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();

        /**
         * 比较一下三个线程池的区别
         * 1. 执行效率
         *     1.1 任务的执行时间非常短的情况，比如就一个操作 i++
         *          - single > fixed > cached
         *     1.2 任务的执行时间不是非常短的情况（我们平时开发大都是这种情况），就算是 sleep 1ms 也是如此
         *          - cached > fixed > single
         *
         * 2. 总结
         *      2.1 影响执行效率的因素：除了线程池自身的参数，还有任务的执行时间、任务是 IO 密集型还是 CPU 密集型
         *          - 任务执行时间短的情况下，single 线程池的效率最高，因为没有线程切换的开销，fixed 线程池次之，cached 线程池最低
         *          - 任务执行时间长的情况下，cached 线程池的效率最高，因为cached 可以创建大量的线程，fixed 线程池次之，single 线程池最低
         *          - 任务是 IO 密集型的情况下，cached 线程池的效率最高，因为cached 可以创建大量的线程，fixed 线程池次之，single 线程池最低
         *          - 任务是 CPU 密集型的情况下，fixed 线程池的效率最高，因为可以复用线程，cached 线程池次之，single 线程池最低
         *          - cached 会创建大量线程，有可能会导致 OOM，fixed 由于线程数固定，所以不会出现OOM 的问题
         *      2.2 线程池的选择
         *          - 阿里巴巴开发手册不推荐使用 Executors 创建线程池，因为 Executors 创建的线程池的参数都是默认的，不够灵活
         *          - 推荐使用 ThreadPoolExecutor 创建线程池，自定义参数
         *
         */
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            MyTask myTask = new MyTask(i);
            executorService.execute(myTask);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("执行时间：" + (System.currentTimeMillis() - start));
    }
}

class MyTask implements Runnable {
    int i = 0;

    public MyTask(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + "程序员做第" + i + "个项目");
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * 自定义拒绝策略
 */
class MyRejectPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义拒绝策略：" + r);
    }
}


