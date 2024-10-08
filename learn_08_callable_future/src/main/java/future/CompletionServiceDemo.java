package future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * ClassName:CompletionServiceDemo
 * Package:future
 * Description: CompletionService 使用案例：先执行完的任务先处理结果
 *
 * @Date:2024/9/30 10:48
 * @Author:qs@1.com
 */
@Slf4j
public class CompletionServiceDemo {

    public static void main(String[] args) {
        // 构建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 构建 CompletionService
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        // 异步向电商S1询价
        completionService.submit(CompletionServiceDemo::getPriceByS1);
        // 异步向电商S2询价
        completionService.submit(CompletionServiceDemo::getPriceByS2);
        // 异步向电商S3询价
        completionService.submit(CompletionServiceDemo::getPriceByS3);

        // 将询价结果异步保存到数据库
        for (int i = 0; i < 3; i++) {
            try {
                /**
                 * 先完成的 Future 先进入FIFO阻塞队列
                 * take()方法获取最先完成的任务
                 */
                Integer r = completionService.take().get();
                executorService.execute(() -> save(r));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private static void save(Integer r) {
        log.debug("保存询价结果:{}",r);
    }

    private static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5000);
        log.debug("电商S1询价等待5秒");
        return 1200;
    }
    private static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(8000);
        log.debug("电商S2询价等待8秒");
        return 1000;
    }
    private static Integer getPriceByS3()  throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(3000);
        log.debug("电商S3询价等待3秒");
        return 800;
    }

}
