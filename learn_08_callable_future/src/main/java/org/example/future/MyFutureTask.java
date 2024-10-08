package org.example.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Fox
 * Description: 自定义FutureTask
 * 使用 wait-notify 机制实现简单的Future功能
 */
@Slf4j
public class MyFutureTask<V> implements Runnable, Future {

    private Callable<V> callable;
    private V result = null;

    public MyFutureTask(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {

        try {
            result = callable.call();
            synchronized (this) {
                // wait-notify机制
                this.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        if (result != null) {
            return result;
        }
        synchronized (this) {
            this.wait();
        }
        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (result != null) {
            return result;
        }
        if (timeout > 0L) {
            unit.sleep(timeout);
            if (result != null) {
                return result;
            } else {
                throw new TimeoutException();
            }
        }
        return result;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyFutureTask task = new MyFutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(3000);
                return "返回任务结果";
            }

        });

        new Thread(task).start();
        log.debug("结果：{}", task.get());
    }

}
