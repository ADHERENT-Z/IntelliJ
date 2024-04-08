package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private volatile int num;
    private Lock lock = new ReentrantLock();

    private int getNum() {
        return num;
    }

    private void addNum() {

        lock.lock();
        try {
            Thread.sleep(5L);
            num++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) throws InterruptedException {

        ReentrantLockDemo demo = new ReentrantLockDemo();
        for (int i = 0; i < 100; i++) {
            new Thread(demo::addNum).start();
        }

        Thread.sleep(3000L);
        System.out.println(demo.getNum());
    }

}
