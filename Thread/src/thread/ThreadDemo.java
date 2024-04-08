package thread;

import java.util.concurrent.*;

public class ThreadDemo {

    public static void main(String[] args) {

        // 继承Thread类
//        InheritThread inheritThread = new InheritThread();
//        inheritThread.start();

        // 将Runnable对象作为任务传入Thread中
//        Thread thread = new Thread(new RunnableThread());
//        thread.start();


        // 使用匿名内部类实现
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("使用Runnable接口，创建匿名内部类实现");
//            }
//        });
//        thread.start();


        // 实现 Callable接口
        // FutureTask底层也实现了 Runnable接口
        FutureTask<String> task = new FutureTask<>(new CallableThread());
        new Thread(task).start();
        try {
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//
//        // 创建一个线程池
//        ExecutorService executorService =
//                new ThreadPoolExecutor(4, 8, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1024));
//        // 提交一个Callable任务
//        Future future = executorService.submit(new CallableThread());
//        // 阻塞获取任务返回结果
//        try {
//            String obj = (String) future.get();
//            System.out.println(obj);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

    }
}
