package thread;

public class YieldDemo {

    public static void main(String[] args) {

        // 让 yield()所在代码行的线程让步,当其他线程先执行
        for (int i = 0; i < 3; i++) {
            final int n = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(n);
                }
            });
            thread.start();
        }

        // 如果活跃的线程数量大于1，main线程让步
        while (Thread.activeCount() > 1) { // 记录活跃线程的数量
            System.out.println("activeCount: " + Thread.activeCount());
            System.out.println("currentThread name: " + Thread.currentThread().getName());
            Thread.yield();
        }

        System.out.println("ok");
    }

}
