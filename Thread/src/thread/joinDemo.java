package thread;

public class joinDemo {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //执行任务，执行时间可能比较长
                for (int i = 0; i < 10 && !Thread.currentThread().isInterrupted(); i++) {
                    System.out.println("run");
                    System.out.println(i);
                    //模拟中断线程
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

        try {
            thread.join(); //当前线程 main线程无条件等待，直到 thread线程执行完毕，当前线程再往后执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("join");
//        try {
//            thread.join(1000); //当前线程等到 1秒，或者等 thread线程执行完毕
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("ok");
    }
}
