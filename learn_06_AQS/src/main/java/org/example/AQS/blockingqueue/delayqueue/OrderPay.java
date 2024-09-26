package org.example.AQS.blockingqueue.delayqueue;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * ClassName:MockOrderUnPayHandler
 * Package:org.example.AQS.blockingqueue.delayqueue
 * Description: 模拟订单支付
 *
 * @Date:2024/9/26 11:12
 * @Author:qs@1.com
 */
public class OrderPay {

    private static String[] str = new String[]{"成功", "支付中", "订单初始化"};
    // 订单超时时间 5 秒
    private static final long ORDER_EXPIRE_TIME_MILLIS = 5000;


    private static String getTime(long currentTimeMillis) {
        // 时间戳转换为时间字符串，格式为：yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTimeMillis);
        return sdf.format(date);
    }

    public static void main(String[] args) {
        OrderOverTimeCloseHandler handler = OrderOverTimeCloseHandler.getInstance();
        handler.init();
        // 模拟多线程下订单支付
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            executorService.execute(() -> {
                int random_index = (int) (Math.random() * str.length);
                long currentTimeMillis = System.currentTimeMillis();
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderNo("20240926000" + finalI);
                orderInfo.setStatus(str[random_index]);
                orderInfo.setCreateTime(currentTimeMillis);
                orderInfo.setExpireTime(currentTimeMillis + ORDER_EXPIRE_TIME_MILLIS);
                handler.addOrder(orderInfo);
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class OrderOverTimeCloseHandler {
        private volatile static OrderOverTimeCloseHandler instance = null;
        /**
         * 守护线程
         */
        private Thread mainThread;
        /**
         * 创建空延时队列
         */
        private final DelayQueue<OrderInfo> queue = new DelayQueue<>();

        private OrderOverTimeCloseHandler() {
        }

        /**
         * 单例模式，双检查锁模式，在并发环境下对象只被初始化一次
         */
        public static OrderOverTimeCloseHandler getInstance() {
            if (instance == null) {
                synchronized (OrderOverTimeCloseHandler.class) {
                    if (instance == null) {
                        instance = new OrderOverTimeCloseHandler();
                    }
                }
            }
            return instance;
        }

        public void init() {
            mainThread = new Thread(this::execute);
            mainThread.setDaemon(true);
            mainThread.setName("OrderOverTimeCloseHandler 守护线程 ---> ");
            mainThread.start();
        }

        /**
         * 读取延时队列，关闭超时订单
         */
        private void execute() {
            while (true) {
                try {
                    if (queue.size() > 0) {
                        //从队列里获取超时的订单
                        OrderInfo orderInfo = queue.take();
                        // 检查订单状态，是否已经成功，成功则将订单从队列中删除。
                        if (Objects.equals(orderInfo.getStatus(), "成功")) {
                            // TODO 支付成功的订单处理逻辑
                            System.out.println("线程：" + Thread.currentThread().getName() + "订单号："
                                    + orderInfo.getOrderNo() + "，订单状态："
                                    + orderInfo.getStatus() + "，订单创建时间："
                                    + getTime(orderInfo.getCreateTime())
                                    + "，订单超时时间：" + getTime(orderInfo.getExpireTime()) + "，当前时间："
                                    + getTime(System.currentTimeMillis()));
                            Thread.sleep(2000);
                        } else {
                            // TODO 超时未支付订单处理逻辑
                            System.out.println("线程：" + Thread.currentThread().getName() + "订单号："
                                    + orderInfo.getOrderNo() + "，变更订单状态为：超时关闭"
                                    + "，订单创建时间："
                                    + getTime(orderInfo.getCreateTime())
                                    + "，订单超时时间：" + getTime(orderInfo.getExpireTime()) + "，当前时间："
                                    + getTime(System.currentTimeMillis()));
                            Thread.sleep(2000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 插入订单到延时队列中
         */
        public void addOrder(OrderInfo orderInfo) {
            System.out.println("订单号：" + orderInfo.getOrderNo()
                    + "，订单状态：" + orderInfo.getStatus()
                    + "，订单创建时间：" + getTime(orderInfo.getCreateTime())
                    + "，订单过期时间：" + getTime(orderInfo.getExpireTime()));
            queue.put(orderInfo);
        }
    }

    @Data
    static class OrderInfo implements Delayed {
        private long createTime; // 订单创建时间
        private long expireTime; // 订单过期时间
        private String orderNo; // 订单号
        private String status; // 订单状态


        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            long d = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
            return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
        }
    }

}
