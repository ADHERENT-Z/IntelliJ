package thread;

public class InterruptDemo {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //执行任务，执行时间可能比较长
                for (int i = 0; i < 30 && !Thread.currentThread().isInterrupted(); i++) {
                    System.out.println(i);
                    //模拟中断线程
                    try {
                        Thread.sleep(1000);
                        //通过标志位自行实现，无法解决线程阻塞导致无法中断
                        //Thread,sleep(100000)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start(); // 线程启动，中断标志位 = false
        System.out.println("thread isInterrupted: " + thread.isInterrupted());
        System.out.println("thread start");

        //模拟，thread执行了 5秒，进程没有结束，要中断，停止 thread线程
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 如果 thread线程处于阻塞状态（休眠等），会抛出 InterruptedException异常
        // 并且会重置 isInterrupt中断标志位位 false

        //告诉 thread线程，要中断（设置 thread线程的中断标志位为 true），由 thread的代码自行决定是否要中断，isInterrupt设置为 true
        thread.interrupt();
        System.out.println("thread isInterrupted: " + thread.isInterrupted());

        System.out.println("thread stop");


    }
}

